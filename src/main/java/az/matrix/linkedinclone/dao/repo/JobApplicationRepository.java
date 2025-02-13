package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.dao.entity.JobApplication;
import az.matrix.linkedinclone.dao.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {
    boolean existsByApplicantAndJob(User applicant, Job job);

    Page<JobApplication> findAllByJob(Job job, Pageable pageable);
}
