package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.dao.entity.JobApplication;
import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.JobApplicationRepo;
import az.matrix.linkedinclone.dao.repo.JobRepo;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.response.JobApplicationResponse;
import az.matrix.linkedinclone.enums.ApplicationStatus;
import az.matrix.linkedinclone.exception.*;
import az.matrix.linkedinclone.exception.IllegalArgumentException;
import az.matrix.linkedinclone.mapper.JobApplicationMapper;
import az.matrix.linkedinclone.service.JobApplicationService;
import az.matrix.linkedinclone.service.OrganizationAdminService;
import az.matrix.linkedinclone.utility.AuthHelper;
import az.matrix.linkedinclone.utility.MediaUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {
    private final JobRepo jobRepo;
    private final UserRepo userRepo;
    private final JobApplicationRepo jobApplicationRepo;
    private final OrganizationAdminService organizationAdminService;
    private final JobApplicationMapper jobApplicationMapper;
    private final AuthHelper authHelper;

    @Value("${files.directory}")
    private String UPLOAD_DIR;

    @Override
    public JobApplicationResponse applyForJob(Long jobId, MultipartFile resume) {
        User user = authHelper.getAuthenticatedUser();
        Job job = jobRepo.findById(jobId).orElseThrow(() -> {
            log.warn("Failed to apply for a job: Job with ID {} not found", jobId);
            return new ResourceNotFoundException("JOB_NOT_FOUND");
        });
        if (jobApplicationRepo.existsByApplicantAndJob(user, job)) {
            log.warn("Failed to apply for job: User with email {} has already applied for job with ID {}", user.getEmail(), jobId);
            throw new AlreadyExistException("ALREADY_APPLIED");
        }
        String resumeUrl = MediaUploadUtil.uploadResume(resume, UPLOAD_DIR);
        JobApplication jobApplication = JobApplication.builder()
                .applicant(user)
                .job(job)
                .applicationDate(LocalDate.now())
                .resumeUrl(resumeUrl)
                .status(ApplicationStatus.PENDING).build();
        jobApplicationRepo.save(jobApplication);
        log.info("Job application for job with ID {} successfully completed for user with email {}", jobId, user.getEmail());
        //mail gelir usere
        return jobApplicationMapper.toDto(jobApplication);
    }

    //top works for you during 7 days
    @Override
    public Page<JobApplicationResponse> getAllJobApplications(Long jobId, Pageable pageable) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(authenticatedUserEmail).orElseThrow(() -> {
            log.warn("Failed to get job applications for job with ID {}: User {} not found", jobId, authenticatedUserEmail);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });

        Job job = jobRepo.findById(jobId).orElseThrow(() -> {
            log.warn("Failed to get job applications for job with ID {}: Job not found with that ID", jobId);
            return new ResourceNotFoundException("JOB_NOT_FOUND");
        });

        Organization organization = job.getOrganization();
        if (!organizationAdminService.isAdmin(user, organization)) {
            log.warn("Failed to get job applications for job with ID {}: You aren't admin of the organization that posted that job", jobId);
            throw new ForbiddenException("NOT_ALLOWED");
        }

        Page<JobApplication> jobApplications = jobApplicationRepo.findAllByJob(job, pageable);
        log.info("Job applications for job {} are returned from db", jobId);
        Page<JobApplicationResponse> jobApplicationResponsePage = jobApplications.map(jobApplicationMapper::toDto);
        log.info("Job application are returned to admin {}", authenticatedUserEmail);

        return jobApplicationResponsePage;
    }

    @Override
    public JobApplicationResponse viewApplication(Long id) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("Operation of getting application with ID {} started by user {}", id, authenticatedUserEmail);
//        User user = userRepo.findByEmail(authenticatedUserEmail).orElseThrow(() -> {
//            log.warn("Failed to get application with ID {}: User {} not found", id, authenticatedUserEmail);
//            return new ResourceNotFoundException("USER_NOT_FOUND");
//        });
//        JobApplication jobApplication = jobApplicationRepo.findById(id).orElseThrow(() -> {
//            log.warn("Failed to get application: Job application with ID {} not found", id);
//            return new ResourceNotFoundException("APPLICATION_NOT_FOUND");
//        });
//        Job job = jobApplication.getJob();
//        Organization organization = job.getOrganization();
//        boolean isAdmin = organizationAdminService.isAdmin(user, organization);
//        if (!isAdmin) {
//            log.warn("Failed to get application: User {} is not admin of organization that posted a job", authenticatedUserEmail);
//            throw new ForbiddenException("NOT_ALLOWED_VIEW_APPLICATION");
//        }
        JobApplication jobApplication = validateAndFetchApplication(id, authenticatedUserEmail);
        if (jobApplication.getStatus() == ApplicationStatus.PENDING) {
            jobApplication.setStatus(ApplicationStatus.REVIEWED);
            //mail gonderilir usere ki applicationu baxilib
            jobApplicationRepo.save(jobApplication);
        }
        JobApplicationResponse jobApplicationResponse = jobApplicationMapper.toDto(jobApplication);
        log.info("Job Application with ID {} successfully returned", id);
        return jobApplicationResponse;
    }

    @Override
    public JobApplicationResponse acceptJobApplication(Long id) {
        return updateStatus(id, ApplicationStatus.ACCEPTED);
    }

    @Override
    public JobApplicationResponse rejectJobApplication(Long id) {
        return updateStatus(id, ApplicationStatus.REJECTED);
    }

    private JobApplicationResponse updateStatus(Long id, ApplicationStatus applicationStatus) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        JobApplication jobApplication = validateAndFetchApplication(id, authenticatedUserEmail);
        if (jobApplication.getStatus() == applicationStatus) {
            log.info("Application with ID {} is already in status {}", id, applicationStatus);
            return jobApplicationMapper.toDto(jobApplication);
        }
        if (jobApplication.getStatus() != ApplicationStatus.REVIEWED) {
            log.warn("Cannot change application status from {} to {}. This transition is not allowed.", jobApplication.getStatus(), applicationStatus);
            throw new IllegalArgumentException("TRANSITION_NOT_ALLOWED");
        }
        jobApplication.setStatus(applicationStatus);
        jobApplicationRepo.save(jobApplication);

        // Send email notification
        String applicantEmail = jobApplication.getApplicant().getEmail();
        String subject = "Your Job Application Status Updated";
        String body = String.format("Dear %s,\n\nYour application for the job '%s' at '%s' has been %s.\n\nThank you.",
                jobApplication.getApplicant().getFirstName(),
                jobApplication.getJob().getTitle(),
                jobApplication.getJob().getOrganization().getName(),
                applicationStatus.name().toLowerCase());

//        emailService.sendEmail(applicantEmail, subject, body);

        log.info("Email sent to {} about status update to {}", applicantEmail, applicationStatus);
        return jobApplicationMapper.toDto(jobApplication);
    }

    private JobApplication validateAndFetchApplication(Long id, String userEmail) {
        log.info("Validating access for job application ID {} by user {}", id, userEmail);

        User user = userRepo.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));

        JobApplication jobApplication = jobApplicationRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("APPLICATION_NOT_FOUND"));

        Organization organization = jobApplication.getJob().getOrganization();
        if (!organizationAdminService.isAdmin(user, organization)) {
            throw new ForbiddenException("NOT_ALLOWED");
        }

        return jobApplication;
    }

}
