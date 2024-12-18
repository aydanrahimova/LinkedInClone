package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepo extends JpaRepository<Job,Long> {
}
