package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Job;
import az.matrix.linkedinclone.enums.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    Optional<Job> findByIdAndStatus(Long id, EntityStatus entityStatus);

    Page<Job> findAll(Specification<Job> specification, Pageable pageable);
}
