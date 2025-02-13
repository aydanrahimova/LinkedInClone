package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Education;
import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.EducationRepository;
import az.matrix.linkedinclone.dao.repo.OrganizationRepository;
import az.matrix.linkedinclone.dao.repo.UserRepository;
import az.matrix.linkedinclone.dto.request.EducationRequest;
import az.matrix.linkedinclone.dto.response.EducationResponse;
import az.matrix.linkedinclone.enums.OrganizationType;
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
    private final UserRepository userRepository;
    private final AuthHelper authHelper;
    private final OrganizationRepository organizationRepository;

    @Override
    public Page<EducationResponse> getEducationsByUserId(Long userId, Pageable pageable) {
        log.info("Getting all education records of the user with ID {} started", userId);
        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException(User.class);
        Page<Education> educations = educationRepository.findAllByUserId(userId, pageable);
        Page<EducationResponse> educationResponsePage = educations.map(educationMapper::toDto);
        log.info("Education records for user with id {} returned.", userId);
        return educationResponsePage;
    }

    @Override
    @Transactional
    public EducationResponse addEducation(EducationRequest educationRequest) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Adding education started by user with ID {}", user.getId());
        Organization school = organizationRepository.findByIdAndOrganizationType(educationRequest.getSchoolId(), OrganizationType.SCHOOL).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
        Education education = educationMapper.toEntity(educationRequest);
        education.setUser(user);
        education.setSchool(school);
        educationRepository.save(education);
        EducationResponse response = educationMapper.toDto(education);
        log.info("New education is added for user with email {}", user.getEmail());
        return response;
    }

    @Override
    @Transactional
    public EducationResponse editEducation(Long educationId, EducationRequest educationRequest) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Editing education with ID {} started by user with ID {}", educationId, user.getId());
        Education education = educationRepository.findByIdAndUser(educationId, user).orElseThrow(() -> new ResourceNotFoundException(Education.class));
        educationMapper.mapForUpdate(education, educationRequest);
        if (!education.getSchool().getId().equals(educationRequest.getSchoolId())) {
            Organization school = organizationRepository.findByIdAndOrganizationType(educationRequest.getSchoolId(), OrganizationType.SCHOOL).orElseThrow(() -> new ResourceNotFoundException(Organization.class));
            education.setSchool(school);
        }
        educationRepository.save(education);
        EducationResponse response = educationMapper.toDto(education);
        log.info("Education successfully edited.");
        return response;
    }

    @Override
    @Transactional
    public void deleteEducation(Long educationId) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Deleting education with ID {} started by user with ID {}", educationId, user.getId());
        Education education = educationRepository.findByIdAndUser(educationId, user).orElseThrow(() -> new ResourceNotFoundException(Education.class));
        educationRepository.delete(education);
        log.info("Education with ID {} deleted successfully by user with ID {}", educationId, user.getId());
    }
}
