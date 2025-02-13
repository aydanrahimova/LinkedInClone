package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Experience;
import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.ExperienceRepository;
import az.matrix.linkedinclone.dao.repo.OrganizationRepository;
import az.matrix.linkedinclone.dao.repo.UserRepository;
import az.matrix.linkedinclone.dto.request.ExperienceRequest;
import az.matrix.linkedinclone.dto.response.ExperienceResponse;
import az.matrix.linkedinclone.enums.OrganizationType;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.ExperienceMapper;
import az.matrix.linkedinclone.service.ExperienceService;
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
@Transactional
public class ExperienceServiceImpl implements ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;
    private final ExperienceMapper experienceMapper;
    private final AuthHelper authHelper;
    private final OrganizationRepository organizationRepository;

    @Override
    public Page<ExperienceResponse> getExperiencesByUserId(Long userId, Pageable pageable) {
        log.info("Getting all experience records of the user with ID {} started", userId);
        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException(User.class);
        Page<Experience> experiences = experienceRepository.findAllByUserId(userId, pageable);
        Page<ExperienceResponse> experienceResponses = experiences.map(experienceMapper::toDto);
        log.info("Experience records for user with ID {} returned", userId);
        return experienceResponses;
    }

    @Override
    @Transactional
    public ExperienceResponse addExperience(ExperienceRequest experienceRequest) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Adding new experience record started by authenticatedUser with ID {}", authenticatedUser.getId());
        Experience experience = experienceMapper.toEntity(experienceRequest);
        Organization company = organizationRepository.findByIdAndOrganizationType(experienceRequest.getCompanyId(), OrganizationType.COMPANY).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
        experience.setCompany(company);
        experience.setUser(authenticatedUser);
        experienceRepository.save(experience);
        ExperienceResponse response = experienceMapper.toDto(experience);
        log.info("New experience record added for authenticatedUser with ID {}", authenticatedUser.getId());
        return response;
    }

    @Override
    @Transactional
    public ExperienceResponse editExperience(Long experienceId, ExperienceRequest experienceRequest) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Operation of editing experience of the user with email {} started.", authenticatedUser.getEmail());
        Experience experience = experienceRepository.findByIdAndUser(experienceId, authenticatedUser).orElseThrow(() -> new ResourceNotFoundException(Experience.class));
        experienceMapper.mapToUpdate(experience, experienceRequest);
        if (!experience.getCompany().getId().equals(experienceRequest.getCompanyId())) {
            Organization company = organizationRepository.findByIdAndOrganizationType(experienceRequest.getCompanyId(), OrganizationType.COMPANY).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
            experience.setCompany(company);
        }
        experienceRepository.save(experience);
        log.info("Experience successfully edited");
        return experienceMapper.toDto(experience);
    }

    @Override
    @Transactional
    public void deleteExperience(Long experienceId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Deleting experience record with ID {} started for authenticatedUser with ID {}", experienceId, authenticatedUser.getId());
        Experience experience = experienceRepository.findByIdAndUser(experienceId, authenticatedUser).orElseThrow(() -> new ResourceNotFoundException(Experience.class));
        experienceRepository.delete(experience);
        log.info("Experience successfully deleted.");
    }
}
