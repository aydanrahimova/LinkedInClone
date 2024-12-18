package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;
    //apply for job
    //get all application-authenticated
    //change status-PENDING to REVIEWED, ACCEPTED, or REJECTED
    @PostMapping("/{jobId}")
    //cv de gonderirik
    public void applyForJob(@PathVariable Long jobId){
        jobApplicationService.applyForJob(jobId);
    }

}
