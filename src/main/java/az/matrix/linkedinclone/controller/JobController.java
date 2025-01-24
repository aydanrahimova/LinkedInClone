package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.JobFilterDto;
import az.matrix.linkedinclone.dto.request.JobRequest;
import az.matrix.linkedinclone.dto.request.JobUpdateRequest;
import az.matrix.linkedinclone.dto.response.JobResponse;
import az.matrix.linkedinclone.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping
    public Page<JobResponse> getJobs(@PageableDefault Pageable pageable, JobFilterDto jobFilterDto) {
        return jobService.getJobs(pageable, jobFilterDto);
    }

    @GetMapping("/{id}")
    public JobResponse getJob(@PathVariable Long id) {
        return jobService.getJob(id);
    }

    @PostMapping
    public JobResponse postJob(@Validated @RequestBody JobRequest jobRequest) {
        return jobService.postJob(jobRequest);
    }

    @PutMapping("/{id}")
    public JobResponse editJob(@PathVariable Long id, @Validated @RequestBody JobUpdateRequest updateRequest) {
        return jobService.editJob(id, updateRequest);
    }

    @PatchMapping("/{id}")
    public JobResponse deactivateJob(@PathVariable Long id){
        return jobService.deactivateJob(id);
    }
}
