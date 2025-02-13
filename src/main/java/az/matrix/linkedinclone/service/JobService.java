package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.JobFilterDto;
import az.matrix.linkedinclone.dto.request.JobRequest;
import az.matrix.linkedinclone.dto.response.JobResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface JobService {
    Page<JobResponse> getJobs(Pageable pageable, JobFilterDto jobFilterDto);

    JobResponse postJob(JobRequest jobRequest);

    JobResponse getJob(Long id);

    JobResponse editJob(Long id, JobRequest jobRequest);

    JobResponse deactivateJob(Long id);
}
