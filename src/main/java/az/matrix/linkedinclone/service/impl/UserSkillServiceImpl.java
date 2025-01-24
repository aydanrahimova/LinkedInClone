package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Skill;
import az.matrix.linkedinclone.dao.entity.SkillUser;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.SkillRepo;
import az.matrix.linkedinclone.dao.repo.SkillUserRepo;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.request.SkillUserRequest;
import az.matrix.linkedinclone.dto.response.SkillUserResponse;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.SkillUserMapper;
import az.matrix.linkedinclone.service.UserSkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSkillServiceImpl implements UserSkillService {
    private final UserRepo userRepo;
    private final SkillUserRepo skillUserRepo;
    private final SkillUserMapper skillUserMapper;
    private final SkillRepo skillRepo;

    @Override
    public Page<SkillUserResponse> getSkillsOfUser(Long userId, Pageable pageable) {
        log.info("Operation of getting skills of user with ID {} started", userId);
        User user = userRepo.findById(userId).orElseThrow(() -> {
            log.warn("Failed to get skills: User with ID {} not found", userId);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        Page<SkillUser> skillUser = skillUserRepo.findAllByUser(user, pageable);
        log.info("Skills of user with ID {} successfully returned", userId);
        return skillUser.map(skillUserMapper::toDto);
    }

    @Override
    public SkillUserResponse addSkillToUser(SkillUserRequest skillUserRequest) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of adding skill with ID {} to user {} started", skillUserRequest.getSkillId(), authenticatedUserEmail);
        User user = userRepo.findByEmail(authenticatedUserEmail).orElseThrow(() -> {
            log.warn("Failed to add skill to user: User {} not found", authenticatedUserEmail);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        Skill skill = skillRepo.findById(skillUserRequest.getSkillId()).orElseThrow(() -> {
            log.warn("Failed to add skill to user: Skill with ID {} not found", skillUserRequest.getSkillId());
            return new ResourceNotFoundException("SKILL_NOT_FOUND");
        });
        SkillUser skillUser = skillUserMapper.toEntity(skillUserRequest);
        skillUser.setUser(user);
        skillUser.setSkill(skill);
        skillUserRepo.save(skillUser);
        log.info("Skill with ID {} successfully added to user {}", skillUserRequest.getSkillId(), authenticatedUserEmail);
        return skillUserMapper.toDto(skillUser);
    }

    @Override
    public SkillUserResponse editSkillOfUser(Long id, SkillUserRequest skillUserRequest) {
//        Skill skill = skillRepo.findBy
        return null;
    }

    @Override
    public void deleteSkillOfUser(Long id) {

    }
}
