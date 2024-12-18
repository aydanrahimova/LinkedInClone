package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Experience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepo extends JpaRepository<Experience, Long> {
    Page<Experience> findAllByUserId(Long userId, Pageable pageable);

    Optional<Experience> findByIdAndUserId(Long experienceId,Long userId);
}
