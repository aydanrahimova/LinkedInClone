package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Experience;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.ExperienceRepo;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.request.ExperienceRequest;
import az.matrix.linkedinclone.dto.response.ExperienceResponse;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.ExperienceMapper;
import az.matrix.linkedinclone.service.ExperienceService;
import az.matrix.linkedinclone.utility.AuthHelper;
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
public class ExperienceServiceImpl implements ExperienceService {
    private final ExperienceRepo experienceRepo;
    private final UserRepo userRepo;
    private final ExperienceMapper experienceMapper;
    private final AuthHelper authHelper;

    @Override
    public Page<ExperienceResponse> getExperiencesByUserId(Long userId, Pageable pageable) {
        log.info("Process of getting another user's experience stared");
        if (!userRepo.existsById(userId)) {
            log.warn("User with id {} not found.", userId);
            throw new ResourceNotFoundException("User not found.");
        }
        Page<ExperienceResponse> experienceDtoList = experienceRepo.findAllByUserId(userId,pageable)
                .map(experienceMapper::toDto);
        log.info("Experience records for user with id {} returned", userId);
        return experienceDtoList;
    }

    @Override
    public ExperienceResponse addExperience(ExperienceRequest experienceRequest) {
        User user = authHelper.getAuthenticatedUser();
        Experience experience = experienceMapper.toEntity(experienceRequest);
        experience.setUser(user);
        experienceRepo.save(experience);
        log.info("New experience to user with email {} is added", user.getEmail());
        return experienceMapper.toDto(experience);
    }

    @Override
    public ExperienceResponse editExperience(Long experienceId, ExperienceRequest experienceRequest) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of editing experience of the user with email {} started.", user.getEmail());
        Experience experience = experienceRepo.findByIdAndUser(experienceId, user)
                .orElseThrow(() -> {
                    log.warn("Failed to edit experience: Experience with ID {} not found or unauthorized access by user with email {}", experienceId, user.getEmail());
                    return new ResourceNotFoundException("EXPERIENCE_NOT_FOUND");
                });
        experienceMapper.mapToUpdate(experience, experienceRequest);
        experienceRepo.save(experience);
        log.info("The process of editing user's experience successfully ended.");
        return experienceMapper.toDto(experience);
    }

    @Override
    public void deleteExperience(Long experienceId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of deleting experience of the user {} is started", currentUserEmail);
        Experience experience = experienceRepo.findByIdAndUserEmail(experienceId, currentUserEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to delete experience: Experience with ID {} not found or unauthorized access by user {}", experienceId, currentUserEmail);
                    return new ResourceNotFoundException("EXPERIENCE_NOT_FOUND");
                });
        experienceRepo.delete(experience);
        log.info("Experience successfully deleted.");
    }
}
