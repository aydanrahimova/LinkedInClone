//package az.matrix.linkedinclone.service.impl;
//
//import az.matrix.linkedinclone.dao.entity.Education;
//import az.matrix.linkedinclone.dao.entity.User;
//import az.matrix.linkedinclone.dao.repo.EducationRepo;
//import az.matrix.linkedinclone.dao.repo.UserRepo;
//import az.matrix.linkedinclone.exception.ResourceNotFoundException;
//import az.matrix.linkedinclone.mapper.EducationMapper;
//import az.matrix.linkedinclone.service.EducationService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//@Transactional
//public class EducationServiceImpl implements EducationService {
//    private final EducationRepo educationRepo;
//    private final EducationMapper educationMapper;
//    private final UserRepo userRepo;
//
//    @Override
//    public List<EducationDto> getAllEducationByUserId(Long userId) {
//        log.info("Getting all education of the user with id {} started", userId);
//        if (!userRepo.existsById(userId)) {
//            log.info("Failed to get education list: User with id {} not found", userId);
//            throw new ResourceNotFoundException("User with ID " + userId + " not found");
//        }
//        List<EducationDto> educationDtoList = educationRepo.findAllByUserId(userId)
//                .stream()
//                .map(educationMapper::toDto)
//                .toList();
//        log.info("Education records for user with id {} returned.", userId);
//        return educationDtoList;
//    }
//
//    @Override
//    public EducationDto addEducation(EducationDto dto) {
//        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("Operation of adding new education to user {} is started.", currentUserEmail);
//        User user = userRepo.findByEmail(currentUserEmail).orElseThrow(() -> {
//            log.warn("Failed to add new education: User {} not found", currentUserEmail);
//            return new ResourceNotFoundException("USER_NOT_FOUND");
//        });
//        Education education = educationMapper.toEntity(dto);
//        education.setUser(user);
//        educationRepo.save(education);
//        log.info("New education is added for user {}", currentUserEmail);
//        return educationMapper.toDto(education);
//    }
//
//    @Override
//    public EducationDto editEducationOfUser(Long educationId, EducationDto educationDto) {
//        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("User {} initiated edition for education ID {}", currentUserEmail, educationId);
//        Education education = educationRepo.findByIdAndUserEmail(educationId, currentUserEmail)
//                .orElseThrow(() -> {
//                    log.warn("Failed to edit education:Education with ID {} not found or unauthorized access by user {}", educationId, currentUserEmail);
//                    return new ResourceNotFoundException("NOT_FOUND");
//                });
//        educationMapper.mapForUpdate(education, educationDto);
//        educationRepo.save(education);
//        log.info("Education successfully edited.");
//        return educationMapper.toDto(education);
//    }
//
//    @Override
//    public void deleteEducationOfUser(Long educationId) {
//        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("User {} initiated deletion for education ID {}", currentUserEmail, educationId);
//        Education education = educationRepo.findByIdAndUserEmail(educationId, currentUserEmail)
//                .orElseThrow(() -> {
//                    log.warn("Failed to delete education:Education with ID {} not found or unauthorized access by user {}", educationId, currentUserEmail);
//                    return new ResourceNotFoundException("NOT_FOUND");
//                });
//        educationRepo.delete(education);
//        log.info("Education with ID {} deleted successfully by user {}", educationId, currentUserEmail);
//    }
//
//
//}
