package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Education;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.EducationRepository;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.request.EducationRequest;
import az.matrix.linkedinclone.dto.response.EducationResponse;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.EducationMapper;
import az.matrix.linkedinclone.service.EducationService;
import az.matrix.linkedinclone.utility.AuthHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class EducationServiceImpl implements EducationService {
    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;
    private final UserRepo userRepo;
    private final AuthHelper authHelper;

    @Override
    public Page<EducationResponse> getEducationsByUserId(Long userId, Pageable pageable) {
        log.info("Getting all education of the user with id {} started", userId);
        if (!userRepo.existsById(userId)) {
            log.info("Failed to get education list: User with id {} not found", userId);
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }
        Page<EducationResponse> educationDtoList = educationRepository
                .findAllByUserId(userId, pageable)
                .map(educationMapper::toDto);
        log.info("Education records for user with id {} returned.", userId);
        return educationDtoList;
    }

    @Override
    public EducationResponse addEducation(EducationRequest dto) {
        User user = authHelper.getAuthenticatedUser();
        Education education = educationMapper.toEntity(dto);
        education.setUser(user);
        educationRepository.save(education);
        log.info("New education is added for user with email {}", user.getEmail());
        return educationMapper.toDto(education);
    }

    @Override
    public EducationResponse editEducation(Long educationId, EducationRequest educationDto) {
        User user = authHelper.getAuthenticatedUser();
        log.info("User with email {} initiated edition for education ID {}", user.getEmail(), educationId);
        Education education = educationRepository.findByIdAndUser(educationId, user)
                .orElseThrow(() -> {
                    log.warn("Failed to edit education:Education with ID {} not found or unauthorized access by user with email {}", educationId, user.getEmail());
                    return new ResourceNotFoundException("NOT_FOUND");
                });
        educationMapper.mapForUpdate(education, educationDto);
        educationRepository.save(education);
        log.info("Education successfully edited.");
        return educationMapper.toDto(education);
    }

    @Override
    public void deleteEducation(Long educationId) {
        User user = authHelper.getAuthenticatedUser();
        log.info("User with email {} initiated deletion for education ID {}", user.getEmail(), educationId);
        Education education = educationRepository.findByIdAndUser(educationId, user)
                .orElseThrow(() -> {
                    log.warn("Failed to delete education:Education with ID {} not found or unauthorized access by user with email {}", educationId, user.getEmail());
                    return new ResourceNotFoundException("NOT_FOUND");
                });
        educationRepository.delete(education);
        log.info("Education with ID {} deleted successfully by user with email {}", educationId, user.getEmail());
    }
}
