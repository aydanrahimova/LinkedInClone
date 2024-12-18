package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.OrganizationAdmin;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.JobRepo;
import az.matrix.linkedinclone.dao.repo.OrganizationAdminRepo;
import az.matrix.linkedinclone.dao.repo.OrganizationRepo;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.request.JobFilterDto;
import az.matrix.linkedinclone.dto.request.JobRequest;
import az.matrix.linkedinclone.dto.request.JobUpdateRequest;
import az.matrix.linkedinclone.dto.response.JobResponse;
import az.matrix.linkedinclone.enums.OrganizationPermission;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.exception.UnauthorizedException;
import az.matrix.linkedinclone.mapper.JobMapper;
import az.matrix.linkedinclone.service.JobService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobMapper jobMapper;
    private final JobRepo jobRepo;
    private final UserRepo userRepo;
    private final OrganizationAdminRepo organizationAdminRepo;
    private final OrganizationRepo organizationRepo;

    @Override
    public Page<JobResponse> getJobs(Pageable pageable, JobFilterDto jobFilterDto) {
        return null;
    }

    @Override
    public JobResponse getJob(Long id) {
        log.info("Operation of getting job with ID {} started", id);
        Job job = jobRepo.findById(id).orElseThrow(() -> {
            log.warn("Failed to get job: Job with ID {} not found", id);
            return new ResourceNotFoundException("JOB_NOT_FOUND");
        });
        log.info("Job with ID {} successfully retrieved", id);
        return jobMapper.toDto(job);
    }

    @Override
    public JobResponse editJob(Long id, JobUpdateRequest updateRequest) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of editing job with ID {} started by user {}",id,authenticatedUserEmail);
        Job job = jobRepo.findById(id)
                .orElseThrow(()->{
                    log.warn("Failed to edit job: Job with ID {} not found",id);
                    return new ResourceNotFoundException("JOB_NOT_FOUND");
                });

        return null;
    }

    @Override
    @Transactional
    public JobResponse postJob(JobRequest jobRequest) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of posting new job started.");
        User user = userRepo
                .findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to post job: User {} not found", authenticatedUserEmail);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });
        Organization organization  = organizationRepo
                .findById(jobRequest.getOrganizationId())
                .orElseThrow(()->{
                    log.warn("Failed to post job: Organization with ID {} not found",jobRequest.getOrganizationId());
                    return new ResourceNotFoundException("ORGANIZATION_NOT_FOUND");
                });
        OrganizationAdmin organizationAdmin = organizationAdminRepo
                .findByAdminAndOrganizationId(user, jobRequest.getOrganizationId())
                .orElseThrow(() -> {
                    log.warn("Failed to post job: User {} is not admin of the organization with ID {} or organization not found", authenticatedUserEmail, jobRequest.getOrganizationId());
                    return new ResourceNotFoundException("NOT_FOUND");
                });
        if (!organizationAdmin.getRole().hasPermission(OrganizationPermission.CONTENT_MANAGE)) {
            log.warn("User {} doesn't have permission to post job", authenticatedUserEmail);
            throw new UnauthorizedException("NOT_ALLOWED");
        }
        Job job = jobMapper.toEntity(jobRequest);
        job.setOrganization(organization);
        jobRepo.save(job);
        log.info("New job for organization with ID {} posted by user {}", jobRequest.getOrganizationId(), authenticatedUserEmail);
        return jobMapper.toDto(job);
    }

}
