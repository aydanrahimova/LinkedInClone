package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Education;
import az.matrix.linkedinclone.dao.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EducationRepository extends JpaRepository<Education,Long> {

    Page<Education> findAllByUserId(Long userId, Pageable pageable);

    Optional<Education> findByIdAndUserId(Long educationId, Long educationId1);

    Optional<Education> findByIdAndUserEmail(Long educationId, String currentUserEmail);

    Optional<Education> findByIdAndUser(Long id, User user);
}
