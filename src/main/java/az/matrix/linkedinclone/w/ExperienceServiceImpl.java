//package az.matrix.linkedinclone.service.impl;
//
//import az.matrix.linkedinclone.dao.entity.Experience;
//import az.matrix.linkedinclone.dao.entity.User;
//import az.matrix.linkedinclone.dao.repo.ExperienceRepo;
//import az.matrix.linkedinclone.dao.repo.UserRepo;
//import az.matrix.linkedinclone.dto.ExperienceDto;
//import az.matrix.linkedinclone.exception.ResourceNotFoundException;
//import az.matrix.linkedinclone.mapper.ExperienceMapper;
//import az.matrix.linkedinclone.service.ExperienceService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//@Transactional
//public class ExperienceServiceImpl implements ExperienceService {
//    private final ExperienceRepo experienceRepo;
//    private final UserRepo userRepo;
//    private final ExperienceMapper experienceMapper;
//
//    @Override
//    public List<ExperienceDto> getAllExperienceByUserId(Long userId) {
//        log.info("Process of getting another user's experience stared");
//        if (!userRepo.existsById(userId)) {
//            log.warn("User with id {} not found.", userId);
//            throw new ResourceNotFoundException("User not found.");
//        }
//        List<ExperienceDto> experienceDtoList = experienceRepo.findAllByUserId(userId)
//                .stream()
//                .map(experienceMapper::toDto)
//                .toList();
//        log.info("Experience records for user with id {} returned", userId);
//        return experienceDtoList;
//    }
//
//    @Override
//    public ExperienceDto addExperience(ExperienceDto experienceDto) {
//        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("Operation of adding new experience to the user {} started.", currentUserEmail);
//        User user = userRepo.findByEmail(currentUserEmail).orElseThrow(() -> {
//            log.warn("User {} not found.", currentUserEmail);
//            return new ResourceNotFoundException("USER_NOT_FOUND");
//        });
//        Experience experience = experienceMapper.toEntity(experienceDto);
//        experience.setUser(user);
//        experienceRepo.save(experience);
//        log.info("New experience to user {} is added", currentUserEmail);
//        return experienceMapper.toDto(experience);
//    }
//
//    @Override
//    public ExperienceDto editExperience(Long experienceId, ExperienceDto experienceDto) {
//        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("Operation of editing experience of the user {} started.", currentUserEmail);
//        Experience experience = experienceRepo.findByIdAndUserEmail(experienceId, currentUserEmail)
//                .orElseThrow(() -> {
//                    log.warn("Failed to edit experience: Experience with ID {} not found or unauthorized access by user {}", experienceId, currentUserEmail);
//                    return new ResourceNotFoundException("EXPERIENCE_NOT_FOUND");
//                });
//        experienceMapper.mapToUpdate(experience, experienceDto);
//        experienceRepo.save(experience);
//        log.info("The process of editing user's experience successfully ended.");
//        return experienceMapper.toDto(experience);
//    }
//
//    @Override
//    public void deleteExperience(Long experienceId) {
//        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("Operation of deleting experience of the user {} is started", currentUserEmail);
//        Experience experience = experienceRepo.findByIdAndUserEmail(experienceId, currentUserEmail)
//                .orElseThrow(() -> {
//                    log.warn("Failed to delete experience: Experience with ID {} not found or unauthorized access by user {}", experienceId, currentUserEmail);
//                    return new ResourceNotFoundException("EXPERIENCE_NOT_FOUND");
//                });
//        experienceRepo.delete(experience);
//        log.info("Experience successfully deleted.");
//    }
//
//
//}
