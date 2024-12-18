package az.matrix.linkedinclone.service;

import org.springframework.stereotype.Service;

@Service
public interface JobApplicationService {
    void applyForJob(Long jobId);
}
