package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.SkillUser;
import az.matrix.linkedinclone.dao.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillUserRepo extends JpaRepository<SkillUser,Long> {
    Page<SkillUser> findAllByUser(User user, Pageable pageable);
}
