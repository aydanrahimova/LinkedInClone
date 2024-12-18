package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Project,Long> {
    Page<Project> findAllByUserId(Long userId, Pageable pageable);

    Optional<Project> findByIdAndUserId(Long projectId,Long userId);
}
