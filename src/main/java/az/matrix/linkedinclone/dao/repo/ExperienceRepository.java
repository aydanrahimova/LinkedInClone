package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Experience;
import az.matrix.linkedinclone.dao.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    Page<Experience> findAllByUserId(Long userId, Pageable pageable);

    Optional<Experience> findByIdAndUser(Long id, User user);
}
