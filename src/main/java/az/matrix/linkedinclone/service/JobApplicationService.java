package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.response.JobApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface JobApplicationService {
    JobApplicationResponse applyForJob(Long jobId, MultipartFile resume);

    Page<JobApplicationResponse> getAllJobApplications(Long jobId, Pageable pageable);

    JobApplicationResponse acceptJobApplication(Long id);

    JobApplicationResponse rejectJobApplication(Long id);

    JobApplicationResponse viewApplication(Long id);
}
