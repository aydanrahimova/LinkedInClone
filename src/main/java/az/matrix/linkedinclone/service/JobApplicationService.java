package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.response.JobApplicationResponse;
import az.matrix.linkedinclone.enums.ApplicationStatus;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface JobApplicationService {
    JobApplicationResponse applyForJob(Long jobId, MultipartFile resume);

    Page<JobApplicationResponse> getAllJobApplications(Long jobId, Pageable pageable);

    JobApplicationResponse changeApplicationStatus(Long id, ApplicationStatus applicationStatus);

    JobApplicationResponse viewApplication(Long id);

    Resource uploadResume(Long id);

}
