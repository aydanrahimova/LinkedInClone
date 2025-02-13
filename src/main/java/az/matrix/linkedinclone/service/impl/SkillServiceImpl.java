package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Skill;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.SkillRepository;
import az.matrix.linkedinclone.dto.request.SkillRequest;
import az.matrix.linkedinclone.dto.response.SkillResponse;
import az.matrix.linkedinclone.exception.AlreadyExistException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.SkillMapper;
import az.matrix.linkedinclone.service.SkillService;
import az.matrix.linkedinclone.utility.AuthHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final AuthHelper authHelper;

    @Override
    public SkillResponse getSkill(Long id) {
        log.info("Getting skill with ID {} started", id);
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Skill.class));
        SkillResponse response = skillMapper.toDto(skill);
        log.info("Skill with ID {} successfully returned", id);
        return response;
    }

    @Override
    public Page<SkillResponse> getPredefinedSkills(Pageable pageable) {
        log.info("Getting all predefined skill started");
        Page<Skill> skills = skillRepository.findAll(pageable);
        Page<SkillResponse> skillPage = skills.map(skillMapper::toDto);
        log.info("All predefined skill returned successfully");
        return skillPage;
    }

    @Override
    @Transactional
    public SkillResponse addPredefinedSkill(SkillRequest skillRequest) {
        User authroticatedUser = authHelper.getAuthenticatedUser();
        log.info("Adding new skill started by admin with ID {}", authroticatedUser.getId());
        if (skillRepository.existsByNameAndCategory(skillRequest.getName(), skillRequest.getCategory())) {
            log.error("Failed to add new skill: Skill with the same name {} and category {} already exists.", skillRequest.getName(), skillRequest.getCategory());
            throw new AlreadyExistException("SKILL_ALREADY_EXISTS");
        }
        Skill skill = skillMapper.toEntity(skillRequest);
        skillRepository.save(skill);
        SkillResponse response = skillMapper.toDto(skill);
        log.info("New skill is successfully added by admin with ID {}", authroticatedUser.getId());
        return response;
    }

    @Override
    @Transactional
    public SkillResponse editPredefinedSkill(Long id, SkillRequest skillRequest) {
        User authroticatedUser = authHelper.getAuthenticatedUser();
        log.info("Editing skill with ID {} started by admin with ID {}", id, authroticatedUser.getId());
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Skill.class));
        if (skillRepository.existsByNameAndCategory(skillRequest.getName(), skillRequest.getCategory())) {
            log.error("Failed to edit skill: Skill with the same name {} and category {} already exists.", skill.getName(), skill.getCategory());
            throw new AlreadyExistException("SKILL_ALREADY_EXISTS");
        }
        skillMapper.mapToUpdate(skill, skillRequest);
        skillRepository.save(skill);
        SkillResponse response = skillMapper.toDto(skill);
        log.info("Skill with ID {} successfully edited by admin with ID {}", id, authroticatedUser.getId());
        return response;
    }

    @Override
    @Transactional
    public void deletePredefinedSkill(Long id) {
        User authroticatedUser = authHelper.getAuthenticatedUser();
        log.info("Deleting skill with ID {} started by admin with ID {}", id, authroticatedUser.getId());
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Skill.class));
        skillRepository.delete(skill);
    }

}
