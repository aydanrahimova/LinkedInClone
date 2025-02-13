package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.SkillUser;
import az.matrix.linkedinclone.dao.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SkillUserRepository extends JpaRepository<SkillUser, Long> {

    Page<SkillUser> findAllByUserId(Long userId, Pageable pageable);

    Optional<SkillUser> findByIdAndUser(Long id, User authenticatedUser);
}
