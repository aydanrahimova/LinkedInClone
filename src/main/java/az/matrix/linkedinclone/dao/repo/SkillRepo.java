package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SkillRepo extends JpaRepository<Skill, Long> {
}
