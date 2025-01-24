package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.Skill;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.JobRepo;
import az.matrix.linkedinclone.dao.repo.OrganizationAdminRepo;
import az.matrix.linkedinclone.dao.repo.OrganizationRepo;
import az.matrix.linkedinclone.dao.repo.SkillRepo;
import az.matrix.linkedinclone.dto.request.JobFilterDto;
import az.matrix.linkedinclone.dto.request.JobRequest;
import az.matrix.linkedinclone.dto.request.JobUpdateRequest;
import az.matrix.linkedinclone.dto.response.JobResponse;
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.exception.ForbiddenException;
import az.matrix.linkedinclone.exception.IllegalArgumentException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.JobMapper;
import az.matrix.linkedinclone.service.JobService;
import az.matrix.linkedinclone.service.specification.JobSpecification;
import az.matrix.linkedinclone.utility.AuthHelper;
import jakarta.transaction.Transactional;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobMapper jobMapper;
    private final JobRepo jobRepo;
    private final OrganizationAdminRepo organizationAdminRepo;
    private final OrganizationRepo organizationRepo;
    private final AuthHelper authHelper;
    private final SkillRepo skillRepo;

    @Override
    public Page<JobResponse> getJobs(Pageable pageable, JobFilterDto jobFilterDto) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of getting jobs started by user {}", authenticatedUserEmail);
        Specification<Job> specification = Specification.where(new JobSpecification(jobFilterDto));
        Page<Job> jobs = jobRepo.findAll(specification, pageable);
        Page<JobResponse> jobResponsePage = jobs.map(jobMapper::toDto);
        log.info("Jobs successfully returned to user {}", authenticatedUserEmail);
        return jobResponsePage;
    }

    @Override
    public JobResponse getJob(Long id) {
        log.info("Operation of getting job with ID {} started", id);
        Job job = jobRepo.findById(id).orElseThrow(() -> {
            log.warn("Failed to get job: Job with ID {} not found", id);
            return new ResourceNotFoundException("JOB_NOT_FOUND");
        });
        log.info("Job with ID {} was successfully retrieved", id);
        return jobMapper.toDto(job);
    }

    @Override
    public JobResponse editJob(Long id, JobUpdateRequest updateRequest) {
        User user = authHelper.getAuthenticatedUser();
        Job job = fetchJobAndCheckAdmin(id, user);
        jobMapper.mapForUpdate(job, updateRequest);
        jobRepo.save(job);
        log.info("Job with ID {} was successfully updated by user with email {}", id, user.getEmail());
        return jobMapper.toDto(job);
    }

    @Override
    public JobResponse deactivateJob(Long id) {
        User user = authHelper.getAuthenticatedUser();
        Job job = fetchJobAndCheckAdmin(id, user);
        if (job.getStatus() != EntityStatus.ACTIVE) {
            log.warn("Cannot deactivate job from {} status. This transition is not allowed.", job.getStatus());
            throw new IllegalArgumentException("TRANSITION_NOT_ALLOWED");
        }
        job.setStatus(EntityStatus.DEACTIVATED);
        jobRepo.save(job);
        log.info("Job with ID {} was successfully deactivated by user with email {}", id, user.getEmail());
        return jobMapper.toDto(job);
    }

    @Override
    @Transactional
    public JobResponse postJob(JobRequest jobRequest) {
        User user = authHelper.getAuthenticatedUser();
        Organization organization = organizationRepo
                .findById(jobRequest.getOrganizationId())
                .orElseThrow(() -> {
                    log.warn("Failed to post job: Organization with ID {} not found", jobRequest.getOrganizationId());
                    return new ResourceNotFoundException("ORGANIZATION_NOT_FOUND");
                });
        boolean isAdmin = organizationAdminRepo.existsByAdminAndOrganization(user, organization);
        if (!isAdmin) {
            log.warn("User with email {} is not admin of the organization,you can't post job.", user.getEmail());
            throw new ForbiddenException("NOT_ALLOWED_POST_JOB");
        }
        Job job = jobMapper.toEntity(jobRequest);
        job.setOrganization(organization);
        job.setStatus(EntityStatus.ACTIVE);
        List<Skill> skills = validateSkills(jobRequest);
        job.setSkills(skills);
        jobRepo.save(job);
        log.info("New job for organization with ID {} posted by user with email {}", jobRequest.getOrganizationId(), user.getEmail());
//        sendNotificationEmail();
        return jobMapper.toDto(job);
    }

    private List<Skill> validateSkills(JobRequest jobRequest) {
        List<Long> skillIds = jobRequest.getSkillId();
        List<Skill> skills = skillRepo.findAllById(skillIds);
        //findAllBy-single query ama iterate elesen n query
        Set<Long> existingSkillIds = skills.stream()
                .map(Skill::getId)
                .collect(Collectors.toSet());

        List<Long> missingSkillIds = skillIds.stream()
                .filter(skillId -> !existingSkillIds.contains(skillId))
                .toList();

        if (!missingSkillIds.isEmpty()) {
            throw new IllegalArgumentException("The following skill IDs do not exist: " + missingSkillIds);
        }
        return skills;
    }

    private Job fetchJobAndCheckAdmin(Long jobId, User user) {
        log.info("Operation started by user with email {}", user.getEmail());
        Job job = jobRepo.findById(jobId).orElseThrow(() -> {
            log.warn("Failed to find job with ID {}", jobId);
            return new ResourceNotFoundException("JOB_NOT_FOUND");
        });
        Organization organization = job.getOrganization();
        boolean isAdmin = organizationAdminRepo.existsByAdminAndOrganization(user, organization);
        if (!isAdmin) {
            log.warn("User with email {} is not an admin of the organization that posted job with ID {}", user.getEmail(), jobId);
            throw new ForbiddenException("NOT_ALLOWED_EDIT_JOB");
        }
        return job;
    }
}
