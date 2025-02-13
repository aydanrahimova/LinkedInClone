package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Skill;
import az.matrix.linkedinclone.dao.entity.SkillUser;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.SkillRepository;
import az.matrix.linkedinclone.dao.repo.SkillUserRepository;
import az.matrix.linkedinclone.dao.repo.UserRepository;
import az.matrix.linkedinclone.dto.request.SkillUserRequest;
import az.matrix.linkedinclone.dto.response.SkillUserResponse;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.SkillUserMapper;
import az.matrix.linkedinclone.service.UserSkillService;
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
public class UserSkillServiceImpl implements UserSkillService {
    private final UserRepository userRepository;
    private final SkillUserRepository skillUserRepository;
    private final SkillUserMapper skillUserMapper;
    private final SkillRepository skillRepository;
    private final AuthHelper authHelper;

    @Override
    public Page<SkillUserResponse> getSkillsByUserId(Long userId, Pageable pageable) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Getting skills of user with ID {} started by user with ID {}", userId, authenticatedUser.getId());
        Page<SkillUser> skillUser = skillUserRepository.findAllByUserId(userId, pageable);
        if (skillUser.isEmpty() && !userRepository.existsById(userId)) throw new ResourceNotFoundException(User.class);
        Page<SkillUserResponse> responsePage = skillUser.map(skillUserMapper::toDto);
        log.info("Skills of user with ID {} successfully returned", userId);
        return responsePage;
    }

    @Override
    @Transactional
    public SkillUserResponse addSkillToUser(SkillUserRequest skillUserRequest) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Adding skill with ID {} to user with ID {} started", skillUserRequest.getSkillId(), authenticatedUser.getId());
        Skill skill = skillRepository.findById(skillUserRequest.getSkillId()).orElseThrow(() -> new ResourceNotFoundException(Skill.class));
        SkillUser skillUser = skillUserMapper.toEntity(skillUserRequest);
        skillUser.setUser(authenticatedUser);
        skillUser.setSkill(skill);
        skillUserRepository.save(skillUser);
        SkillUserResponse response = skillUserMapper.toDto(skillUser);
        log.info("Skill with ID {} successfully added to user {}", skillUserRequest.getSkillId(), authenticatedUser.getId());
        return response;
    }

    @Override
    @Transactional
    public SkillUserResponse editSkillOfUser(Long id, SkillUserRequest skillUserRequest) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Editing user skill with ID {} to user with ID {} started", id, authenticatedUser.getId());
        SkillUser skillUser = skillUserRepository.findByIdAndUser(id, authenticatedUser).orElseThrow(() -> new ResourceNotFoundException(SkillUser.class));
        if (!skillUser.getSkill().getId().equals(skillUserRequest.getSkillId())) {
            Skill skill = skillRepository.findById(skillUserRequest.getSkillId()).orElseThrow(() -> new ResourceNotFoundException(Skill.class));
            skillUser.setSkill(skill);
        }
        skillUserMapper.mapToUpdate(skillUser, skillUserRequest);
        SkillUserResponse response = skillUserMapper.toDto(skillUser);
        log.info("User Skill successfully edited");
        return response;
    }

    @Override
    @Transactional
    public void deleteSkillOfUser(Long id) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Deleting user skill with ID {} to user with ID {} started", id, authenticatedUser.getId());
        SkillUser skillUser = skillUserRepository.findByIdAndUser(id, authenticatedUser).orElseThrow(() -> new ResourceNotFoundException(SkillUser.class));
        skillUserRepository.delete(skillUser);
        log.info("User skill successfully deleted");
    }
}
