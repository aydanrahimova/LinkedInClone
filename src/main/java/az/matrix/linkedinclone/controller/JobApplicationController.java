package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.response.JobApplicationResponse;
import az.matrix.linkedinclone.enums.ApplicationStatus;
import az.matrix.linkedinclone.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @GetMapping
    public Page<JobApplicationResponse> getJobApplications(@RequestParam Long jobId, @PageableDefault Pageable pageable) {
        return jobApplicationService.getAllJobApplications(jobId, pageable);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JobApplicationResponse applyForJob(@RequestParam Long jobId, @RequestPart MultipartFile resume) {
        return jobApplicationService.applyForJob(jobId,resume);
    }

    @GetMapping("/{id}")
    public JobApplicationResponse viewApplication(@PathVariable Long id) {
        return jobApplicationService.viewApplication(id);
    }

    @PatchMapping("/{id}/status")
    public JobApplicationResponse acceptJobApplication(@PathVariable Long id, @RequestParam ApplicationStatus applicationStatus) {
        return jobApplicationService.changeApplicationStatus(id,applicationStatus);
    }

    @PostMapping("/{id}/upload-resume")
    public String uploadResume(@PathVariable Long id){
        return jobApplicationService.uploadResume(id);
    }
}
