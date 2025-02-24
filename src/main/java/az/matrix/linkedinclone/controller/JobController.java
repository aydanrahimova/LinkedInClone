package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.JobFilterDto;
import az.matrix.linkedinclone.dto.request.JobRequest;
import az.matrix.linkedinclone.dto.response.JobResponse;
import az.matrix.linkedinclone.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<Page<JobResponse>> getJobs(@PageableDefault Pageable pageable, JobFilterDto jobFilterDto) {
        Page<JobResponse> jobs = jobService.getJobs(pageable, jobFilterDto);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJob(@PathVariable Long id) {
        JobResponse jobResponse = jobService.getJob(id);
        return ResponseEntity.ok(jobResponse);
    }

    @PostMapping
    public ResponseEntity<JobResponse> postJob(@Validated @RequestBody JobRequest jobRequest) {
        JobResponse jobResponse = jobService.postJob(jobRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> editJob(@PathVariable Long id, @Validated @RequestBody JobRequest updateRequest) {
        JobResponse jobResponse = jobService.editJob(id, updateRequest);
        return ResponseEntity.ok(jobResponse);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<JobResponse> deactivateJob(@PathVariable Long id) {
        JobResponse jobResponse = jobService.deactivateJob(id);
        return ResponseEntity.ok(jobResponse);
    }
}
