package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Project;
import az.matrix.linkedinclone.dao.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByUserId(Long userId, Pageable pageable);

    Optional<Project> findByIdAndUser(Long id, User user);
}
