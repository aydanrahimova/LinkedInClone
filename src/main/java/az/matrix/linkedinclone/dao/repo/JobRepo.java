package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.dao.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobRepo extends JpaRepository<Job,Long>, JpaSpecificationExecutor<Job> {
}
