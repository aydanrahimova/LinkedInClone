package az.matrix.linkedinclone.dao.repo;

import az.matrix.linkedinclone.dao.entity.Skill;
import az.matrix.linkedinclone.enums.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;


public interface SkillRepo extends JpaRepository<Skill, Long> {
    boolean existsByNameAndCategory(String name, SkillCategory category);

//    List<Skill> findAllByUserId(Long userId);
}
