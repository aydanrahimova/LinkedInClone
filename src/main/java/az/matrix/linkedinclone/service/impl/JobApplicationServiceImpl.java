package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.dao.entity.JobApplication;
import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.JobApplicationRepository;
import az.matrix.linkedinclone.dao.repo.JobRepository;
import az.matrix.linkedinclone.dto.response.JobApplicationResponse;
import az.matrix.linkedinclone.enums.ApplicationStatus;
import az.matrix.linkedinclone.enums.EmailTemplate;
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.exception.*;
import az.matrix.linkedinclone.exception.IllegalArgumentException;
import az.matrix.linkedinclone.mapper.JobApplicationMapper;
import az.matrix.linkedinclone.service.EmailSenderService;
import az.matrix.linkedinclone.service.JobApplicationService;
import az.matrix.linkedinclone.service.OrganizationAdminService;
import az.matrix.linkedinclone.utility.AuthHelper;
import az.matrix.linkedinclone.utility.MediaUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationMapper jobApplicationMapper;
    private final OrganizationAdminService organizationAdminService;
    private final AuthHelper authHelper;
    private final EmailSenderService emailSenderService;

    @Value("${files.directory}")
    private String UPLOAD_DIR;

    @Override
    public JobApplicationResponse applyForJob(Long jobId, MultipartFile resume) {
        User user = authHelper.getAuthenticatedUser();
        Job job = jobRepository.findByIdAndStatus(jobId, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Job.class));
        if (jobApplicationRepository.existsByApplicantAndJob(user, job)) {
            log.warn("Failed to apply for job: User with ID {} has already applied for job with ID {}", user.getId(), jobId);
            throw new AlreadyExistException("ALREADY_APPLIED");
        }
        String resumeUrl = MediaUploadUtil.uploadResume(resume, UPLOAD_DIR);
        JobApplication jobApplication = JobApplication.builder()
                .applicant(user)
                .job(job)
                .resumeUrl(resumeUrl)
                .status(ApplicationStatus.PENDING)
                .build();
        jobApplicationRepository.save(jobApplication);
        JobApplicationResponse response = jobApplicationMapper.toDto(jobApplication);
        log.info("Job application for job with ID {} successfully completed for user with email {}", jobId, user.getEmail());
        Map<String, String> placeholders = Map.of("userName", user.getFirstName(), "organizationName", job.getOrganization().getName());
        emailSenderService.sendEmail(user.getEmail(), EmailTemplate.APPLICATION_SEND, placeholders);
        return response;
    }

    @Override
    public Page<JobApplicationResponse> getAllJobApplications(Long jobId, Pageable pageable) {
        User user = authHelper.getAuthenticatedUser();
        Job job = jobRepository.findByIdAndStatus(jobId, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(Job.class));
        Organization organization = job.getOrganization();
        if (!organizationAdminService.isAdmin(user, organization.getId())) {
            log.warn("Failed to get job applications for job with ID {}: You aren't admin of the organization that posted that job", jobId);
            throw new ForbiddenException("NOT_ALLOWED");
        }
        Page<JobApplication> jobApplications = jobApplicationRepository.findAllByJob(job, pageable);
        Page<JobApplicationResponse> jobApplicationResponsePage = jobApplications.map(jobApplicationMapper::toDto);
        log.info("Job application are returned to user with ID {}", user.getId());
        return jobApplicationResponsePage;
    }

    @Override
    public JobApplicationResponse viewApplication(Long id) {
        User user = authHelper.getAuthenticatedUser();
        JobApplication jobApplication = validateJobApplicationAndAdminPermissions(id, user);
        if (jobApplication.getStatus() == ApplicationStatus.PENDING) {
            jobApplication.setStatus(ApplicationStatus.REVIEWED);
            Map<String, String> placeholders = Map.of("organizationName", jobApplication.getJob().getOrganization().getName());
            emailSenderService.sendEmail(jobApplication.getApplicant().getEmail(), EmailTemplate.APPLICATION_VIEWED, placeholders);
            jobApplicationRepository.save(jobApplication);
        }
        JobApplicationResponse response = jobApplicationMapper.toDto(jobApplication);
        log.info("Job Application with ID {} successfully returned", id);
        return response;
    }

    @Override
    public JobApplicationResponse changeApplicationStatus(Long id, ApplicationStatus applicationStatus) {
        User user = authHelper.getAuthenticatedUser();
        JobApplication jobApplication = validateJobApplicationAndAdminPermissions(id, user);
        if (jobApplication.getStatus() == applicationStatus) {
            log.info("Application with ID {} is already in status {}", id, applicationStatus);
            return jobApplicationMapper.toDto(jobApplication);
        }
        if (applicationStatus != ApplicationStatus.ACCEPTED && applicationStatus != ApplicationStatus.REJECTED) {
            log.error("Invalid status transition to {}. Applications can only be changed to ACCEPTED or REJECTED.", applicationStatus);
            throw new IllegalArgumentException("INVALID_STATUS_TRANSITION");
        }
        if (jobApplication.getStatus() != ApplicationStatus.REVIEWED) {
            log.error("Cannot change application status from {} to {}. This transition is not allowed.", jobApplication.getStatus(), applicationStatus);
            throw new IllegalArgumentException("TRANSITION_NOT_ALLOWED");
        }
        jobApplication.setStatus(applicationStatus);
        jobApplicationRepository.save(jobApplication);

        Map<String, String> placeholders = Map.of(
                "userName", jobApplication.getApplicant().getFirstName(),
                "jobTitle", jobApplication.getJob().getTitle(), "organizationName",
                jobApplication.getJob().getOrganization().getName());
        if (jobApplication.getStatus() != ApplicationStatus.ACCEPTED)
            emailSenderService.sendEmail(jobApplication.getApplicant().getEmail(), EmailTemplate.APPLICATION_REJECT, placeholders);
        else
            emailSenderService.sendEmail(jobApplication.getApplicant().getEmail(), EmailTemplate.APPLICATION_ACCEPTED, placeholders);


        JobApplicationResponse response = jobApplicationMapper.toDto(jobApplication);
        log.info("Status for application with ID {} changed to {}", id, applicationStatus);
        return response;
    }

    private JobApplication validateJobApplicationAndAdminPermissions(Long id, User user) {
        JobApplication jobApplication = jobApplicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(JobApplication.class));
        Organization organization = jobApplication.getJob().getOrganization();
        if (!organizationAdminService.isAdmin(user, organization.getId())) {
            log.warn("User with ID {} is not an admin of the organization that posted job application with ID {}", user.getId(), id);
            throw new ForbiddenException("NOT_ALLOWED");
        }
        return jobApplication;
    }


    @Override
    public Resource uploadResume(Long id) {
        JobApplication jobApplication = jobApplicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(JobApplication.class));
        String resumeUrl = jobApplication.getResumeUrl();
        Path path = Paths.get(resumeUrl);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("File not found: " + resumeUrl);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file path: " + resumeUrl);
        }
        return resource;
    }


}
