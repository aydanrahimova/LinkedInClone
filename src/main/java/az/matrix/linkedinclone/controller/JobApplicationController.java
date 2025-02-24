package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.response.JobApplicationResponse;
import az.matrix.linkedinclone.enums.ApplicationStatus;
import az.matrix.linkedinclone.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @GetMapping
    public ResponseEntity<Page<JobApplicationResponse>> getJobApplications(@RequestParam Long jobId, @PageableDefault Pageable pageable) {
        Page<JobApplicationResponse> jobApplications = jobApplicationService.getAllJobApplications(jobId, pageable);
        return ResponseEntity.ok(jobApplications);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JobApplicationResponse> applyForJob(@RequestParam Long jobId, @RequestPart MultipartFile resume) {
        JobApplicationResponse jobApplicationResponse = jobApplicationService.applyForJob(jobId, resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobApplicationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> viewApplication(@PathVariable Long id) {
        JobApplicationResponse jobApplicationResponse = jobApplicationService.viewApplication(id);
        return ResponseEntity.ok(jobApplicationResponse);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplicationResponse> acceptJobApplication(@PathVariable Long id, @RequestParam ApplicationStatus applicationStatus) {
        JobApplicationResponse jobApplicationResponse = jobApplicationService.changeApplicationStatus(id, applicationStatus);
        return ResponseEntity.ok(jobApplicationResponse);
    }

    @PostMapping("/{id}/upload-resume")
    public ResponseEntity<Resource> uploadResume(@PathVariable Long id) {
        Resource resource = jobApplicationService.uploadResume(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
