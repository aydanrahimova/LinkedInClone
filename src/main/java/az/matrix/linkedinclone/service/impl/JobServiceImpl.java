package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.Skill;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.JobRepository;
import az.matrix.linkedinclone.dao.repo.OrganizationRepository;
import az.matrix.linkedinclone.dao.repo.SkillRepository;
import az.matrix.linkedinclone.dto.request.JobFilterDto;
import az.matrix.linkedinclone.dto.request.JobRequest;
import az.matrix.linkedinclone.dto.response.JobResponse;
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.exception.ForbiddenException;
import az.matrix.linkedinclone.exception.IllegalArgumentException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.JobMapper;
import az.matrix.linkedinclone.service.JobService;
import az.matrix.linkedinclone.service.OrganizationAdminService;
import az.matrix.linkedinclone.service.specification.JobSpecification;
import az.matrix.linkedinclone.utility.AuthHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobMapper jobMapper;
    private final JobRepository jobRepository;
    private final OrganizationRepository organizationRepository;
    private final AuthHelper authHelper;
    private final SkillRepository skillRepository;
    private final OrganizationAdminService organizationAdminService;

    @Override
    public Page<JobResponse> getJobs(Pageable pageable, JobFilterDto jobFilterDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Getting jobs started by user with ID {}", authenticatedUser.getId());
        Specification<Job> specification = Specification.where(new JobSpecification(jobFilterDto));
        Page<Job> jobs = jobRepository.findAll(specification, pageable);
        Page<JobResponse> jobResponsePage = jobs.map(jobMapper::toDto);
        log.info("Jobs successfully returned");
        return jobResponsePage;
    }

    @Override
    public JobResponse getJob(Long id) {
        log.info("Getting job with ID {} started", id);
        Job job = jobRepository.findByIdAndStatus(id, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Job.class));
        JobResponse response = jobMapper.toDto(job);
        log.info("Job with ID {} successfully returned", id);
        return response;
    }

    @Override
    @Transactional
    public JobResponse postJob(JobRequest jobRequest) {
        User user = authHelper.getAuthenticatedUser();
        Organization organization = organizationRepository.findByIdAndStatus(jobRequest.getOrganizationId(), EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
        if (!organizationAdminService.isAdmin(user, organization.getId())) {
            log.error("User with ID {} can't post job:User is not admin of the organization,you .", user.getId());
            throw new ForbiddenException("NOT_ALLOWED_POST_JOB");
        }
        Job job = jobMapper.toEntity(jobRequest);
        job.setOrganization(organization);
        job.setStatus(EntityStatus.ACTIVE);
        if (jobRequest.getSkillId() != null && !jobRequest.getSkillId().isEmpty()) {
            List<Skill> skills = validateSkills(jobRequest.getSkillId());
            job.setSkills(skills);
        }
        jobRepository.save(job);
        JobResponse response = jobMapper.toDto(job);
        log.info("New job for successfully posted by user with ID {} for organization with ID {}", user.getId(), organization.getId());
        return response;
    }


    @Override
    @Transactional
    public JobResponse editJob(Long id, JobRequest updateRequest) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Editing job operation started by user with ID {}", user.getId());
        Job job = validateJobAndAdminPermissions(id, user);
        jobMapper.mapForUpdate(job, updateRequest);
        if (updateRequest.getSkillId() != null && !updateRequest.getSkillId().isEmpty()) {
            List<Skill> skills = validateSkills(updateRequest.getSkillId());
            job.setSkills(skills);
        }
        jobRepository.save(job);
        log.info("Job with ID {} was successfully updated by user with ID {}", id, user.getId());
        return jobMapper.toDto(job);
    }

    @Override
    @Transactional
    public JobResponse deactivateJob(Long id) {
        User user = authHelper.getAuthenticatedUser();
        Job job = validateJobAndAdminPermissions(id, user);
        if (job.getStatus() != EntityStatus.ACTIVE) {
            log.warn("Cannot deactivate job from {} status. This transition is not allowed.", job.getStatus());
            throw new IllegalArgumentException("TRANSITION_NOT_ALLOWED");
        }
        job.setStatus(EntityStatus.DEACTIVATED);
        jobRepository.save(job);
        log.info("Job with ID {} was successfully deactivated by user with ID {}", id, user.getId());
        return jobMapper.toDto(job);
    }


    private List<Skill> validateSkills(List<Long> skillIds) {
        List<Skill> skills = skillRepository.findAllById(skillIds);
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

    private Job validateJobAndAdminPermissions(Long jobId, User user) {
        log.info("Operation started by user with email {}", user.getEmail());
        Job job = jobRepository.findByIdAndStatus(jobId, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Job.class));
        Organization organization = job.getOrganization();
        if (!organizationAdminService.isAdmin(user, organization.getId())) {
            log.warn("User with ID {} is not an admin of the organization that posted job with ID {}", user.getId(), jobId);
            throw new ForbiddenException("NOT_ALLOWED_EDIT_JOB");
        }
        return job;
    }
}
