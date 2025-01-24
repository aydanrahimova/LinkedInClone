package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Skill;
import az.matrix.linkedinclone.dao.repo.SkillRepo;
import az.matrix.linkedinclone.dto.request.SkillRequest;
import az.matrix.linkedinclone.dto.response.SkillResponse;
import az.matrix.linkedinclone.exception.AlreadyExistException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.SkillMapper;
import az.matrix.linkedinclone.service.SkillService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SkillServiceImpl implements SkillService {
    private final SkillRepo skillRepo;
    private final SkillMapper skillMapper;

    @Override
    public SkillResponse getSkill(Long id) {
        String currentAdminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of getting skill with ID {} started by admin {}", id, currentAdminEmail);
        Skill skill = skillRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to get skill: Skill with ID {} not found", id);
                    return new ResourceNotFoundException("SKILL_NOT_FOUND");
                });
        log.info("Skill with ID {} successfully returned", id);
        return skillMapper.toDto(skill);
    }

    @Override
    public Page<SkillResponse> getPredefinedSkills(Pageable pageable) {
        String currentAdminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of getting all predefined skill started by admin {}", currentAdminEmail);
        Page<Skill> skills = skillRepo.findAll(pageable);
        Page<SkillResponse> skillPage = skills.map(skillMapper::toDto);
        log.info("All predefined skill returned successfully");
        return skillPage;
    }

    @Override
    @Transactional
    public SkillResponse addPredefinedSkill(SkillRequest skillRequest) {
        String currentAdminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of adding new skill started by admin {}", currentAdminEmail);
        Skill skill = skillMapper.toEntity(skillRequest);
        if (skillRepo.existsByNameAndCategory(skill.getName(), skill.getCategory())) {
            log.warn("Failed to add new skill: Skill with the same name {} and category {} already exists.", skill.getName(), skill.getCategory());
            throw new AlreadyExistException("SKILL_ALREADY_EXISTS");
        }
        skillRepo.save(skill);
        log.info("New skill is successfully added by admin {}", currentAdminEmail);
        return skillMapper.toDto(skill);
    }

    @Override
    @Transactional
    public SkillResponse editPredefinedSkill(Long id, SkillRequest skillRequest) {
        String currentAdminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of editing skill with ID {} started by admin {}", id, currentAdminEmail);
        Skill skill = skillRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to edit skill: Skill with ID {} not found", id);
                    return new ResourceNotFoundException("SKILL_NOT_FOUND");
                });
        skillMapper.mapToUpdate(skill, skillRequest);
        if (skillRepo.existsByNameAndCategory(skill.getName(), skill.getCategory())) {
            log.warn("Failed to edit skill: Skill with the same name {} and category {} already exists.", skill.getName(), skill.getCategory());
            throw new AlreadyExistException("SKILL_ALREADY_EXISTS");
        }
        skillRepo.save(skill);
        log.info("Skill with ID {} successfully edited by admin {}", id, currentAdminEmail);
        return skillMapper.toDto(skill);
    }

    @Override
    @Transactional
    public void deletePredefinedSkill(Long id) {
        String currentAdminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of deletion skill with ID {} started by admin {}", id, currentAdminEmail);
        Skill skill = skillRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to delete skill: Skill with ID {} not found", id);
                    return new ResourceNotFoundException("SKILL_NOT_FOUND");
                });
        skillRepo.delete(skill);
    }


}
