package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Education;
import az.matrix.linkedinclone.dao.entity.Experience;
import az.matrix.linkedinclone.dao.entity.Project;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.EducationRepo;
import az.matrix.linkedinclone.dao.repo.ExperienceRepo;
import az.matrix.linkedinclone.dao.repo.ProjectRepo;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.request.*;
import az.matrix.linkedinclone.dto.response.EducationResponse;
import az.matrix.linkedinclone.dto.response.ExperienceResponse;
import az.matrix.linkedinclone.dto.response.ProjectResponse;
import az.matrix.linkedinclone.dto.response.UserDetailsResponse;
import az.matrix.linkedinclone.enums.ProfileStatus;
import az.matrix.linkedinclone.exception.FileIOException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.EducationMapper;
import az.matrix.linkedinclone.mapper.ExperienceMapper;
import az.matrix.linkedinclone.mapper.ProjectMapper;
import az.matrix.linkedinclone.mapper.UserMapper;
import az.matrix.linkedinclone.service.UserService;
import az.matrix.linkedinclone.utility.AuthUtil;
import az.matrix.linkedinclone.utility.UploadPathUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {


    @Value("${files.directory}")
    private String UPLOAD_DIR;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtil authUtil;
    private final EducationRepo educationRepo;
    private final EducationMapper educationMapper;
    private final ExperienceRepo experienceRepo;
    private final ExperienceMapper experienceMapper;
    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;

    @Override
    public UserDetailsResponse getById(Long id) {
        log.info("Attempting to get profile of user with ID {}", id);
        User user = userRepo.findByIdAndStatus(id,ProfileStatus.ACTIVE)
                .orElseThrow(() -> {
                    log.warn("Failed to get user: User with ID {} not found or not active user", id);
                    return new ResourceNotFoundException("USER_NOT_FOUND_OR_NOT_ACTIVE");
                });
        log.info("Successfully retrieved user with ID {}", id);
        return userMapper.toDetailsResponse(user);
    }

    @Override
    @Transactional
    public UserDetailsResponse editUserInfo(UserDetailsRequest userDetailsRequest) throws FileIOException, IOException {
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Editing user {} is started.", authenticatedEmail);
        User existingUser = userRepo.findByEmail(authenticatedEmail).orElseThrow(() -> {
            log.warn("Failed to edit user profile: User {} not found", authenticatedEmail);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        updatePhoto(userDetailsRequest, existingUser);
        userMapper.mapForUpdate(existingUser, userDetailsRequest);
        userRepo.save(existingUser);
        log.info("User {} successfully updated.", authenticatedEmail);
        return userMapper.toDetailsResponse(existingUser);
    }

    private void updatePhoto(UserDetailsRequest userDetailsRequest, User existingUser) throws FileIOException, IOException {
        MultipartFile photo = userDetailsRequest.getPhoto();
        if (photo != null && !photo.isEmpty()) {
            deleteExistingPhoto(existingUser);
            String userDirName = String.format("%d_%s_%s", existingUser.getId(), existingUser.getFirstName(), existingUser.getLastName());
            Path userDir = Paths.get(UPLOAD_DIR, userDirName);
            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }
            String photoPath = UploadPathUtility.uploadPath(photo, UPLOAD_DIR, existingUser.getId(), existingUser.getFirstName(), existingUser.getLastName());
            existingUser.setPhotoUrl(photoPath);
        }
    }

    private void deleteExistingPhoto(User existingUser) {
        if(existingUser.getPhotoUrl()!=null){
            Path existingPhotoPath = Paths.get(existingUser.getPhotoUrl());
            try {
                Files.deleteIfExists(existingPhotoPath);
            } catch (IOException e) {
                log.warn("Failed to delete existing photo: {}", existingPhotoPath, e);
            }
        }
    }


    @Override
    @Transactional
    public void changePassword(ChangePasswordDto changePasswordDto) {
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Changing password operation is started for user {}.", authenticatedEmail);
        User user = userRepo.findByEmail(authenticatedEmail).orElseThrow(() -> {
            log.warn("Failed to change password: User {} not found", authenticatedEmail);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        if (changePasswordDto.getNewPassword().equals(changePasswordDto.getRetryPassword()) &&
                passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepo.save(user);
            log.info("Password for user {} changed.", authenticatedEmail);
        } else {
            log.error("Failed to change password");
            throw new IllegalArgumentException("Old password entered incorrectly or new passwords do not match");
        }
    }

    @Override
    public void deactivateUser(String password) {
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(authenticatedEmail);
        log.info("Operation of deactivation started for user {}", authenticatedEmail);
        User user = userRepo.findByEmail(authenticatedEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to inactivate user: User {} not found", authenticatedEmail);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });
        if (passwordEncoder.matches(password, user.getPassword())) {
            if (user.getStatus() == ProfileStatus.ACTIVE) {
                user.setStatus(ProfileStatus.DEACTIVATED);
                user.setDeactivationDate(LocalDate.now());
                userRepo.save(user);
            } else {
                log.warn("User {} is already deactivated or deleted.", authenticatedEmail);
                throw new IllegalStateException("USER_ALREADY_DELETED_OR_DEACTIVATED");
            }
        } else {
            log.warn("Failed to deactivated user {}: Incorrect password", authenticatedEmail);
            throw new IllegalArgumentException("INCORRECT_PASSWORD");
        }

    }

    @Override
    public void deleteUser() {
        List<User> users = userRepo.findAllByStatus(ProfileStatus.DEACTIVATED);
        LocalDate threshold = LocalDate.now().minusDays(30);
        for (User user : users) {
            if (user.getDeactivationDate().isBefore(threshold)) {
                user.setStatus(ProfileStatus.DELETED);
                user.setDeactivationDate(null);
                userRepo.save(user);
            }
        }
    }


    @Override
    public Page<EducationResponse> getAllEducation(Long id, Pageable pageable) {
        log.info("Getting all education of the user with ID {} started", id);
        if (!userRepo.existsById(id)) {
            log.info("Failed to get education list: User with ID {} not found", id);
            throw new ResourceNotFoundException("USER_NOT_FOUND");
        }
        Page<Education> educations = educationRepo.findAllByUserId(id, pageable);
        log.info("Education records for user with ID {} returned.", id);
        return educations.map(educationMapper::toDto);
    }

    @Override
    @Transactional
    public EducationResponse addEducation(Long id, HttpServletRequest request, EducationRequest educationRequest) {
        Long authenticatedId = authUtil.resolveAndAuthorizeUser(request, id);
        log.info("Operation of adding new education for user with ID {} is started.", authenticatedId);
        User user = userRepo.findById(authenticatedId).orElseThrow(() -> {
            log.warn("Failed to add new education: User with ID {} not found", authenticatedId);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        Education education = educationMapper.toEntity(educationRequest);
        education.setUser(user);
        educationRepo.save(education);
        log.info("New education is added for user with ID {}", authenticatedId);
        return educationMapper.toDto(education);
    }


    //2ci versiya
    public EducationResponse addEducation2(Long id, HttpServletRequest request, EducationRequest educationRequest) {
        Long authenticatedId = authUtil.resolveAndAuthorizeUser(request, id);
        log.info("Operation of adding new education for user with ID {} is started.", authenticatedId);
        User user = userRepo.findById(authenticatedId).orElseThrow(() -> {
            log.warn("Failed to add new education: User with ID {} not found", authenticatedId);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        Education education = educationMapper.toEntity(educationRequest);
        user.getEducation().add(education);
        userRepo.save(user);
        return educationMapper.toDto(education);
    }

    @Override//tam deyil
    @Transactional
    public EducationResponse editEducation(Long id, Long educationId, HttpServletRequest request, EducationRequest educationRequest) {
        Long authenticatedId = authUtil.resolveAndAuthorizeUser(request, id);
        log.info("User with ID {} initiated edition for education ID {}", authenticatedId, educationId);

        Education education = educationRepo.findByIdAndUserId(educationId, authenticatedId)
                .orElseThrow(() -> {
                    log.warn("Failed to edit education: Education with ID {} not found or unauthorized access by user with ID {}", educationId, authenticatedId);
                    return new ResourceNotFoundException("NOT_FOUND");
                });
        educationMapper.mapForUpdate(education, educationRequest);
        educationRepo.save(education);
        log.info("Education successfully edited.");
        return educationMapper.toDto(education);
    }

    //2ci versiya
    public EducationResponse editEducation2(Long id, Long educationId, HttpServletRequest request, EducationRequest educationRequest) {
        Long authenticatedId = authUtil.resolveAndAuthorizeUser(request, id);
        log.info("User with ID {} initiated edition for education ID {}", authenticatedId, educationId);
        User user = userRepo.findById(authenticatedId).orElseThrow(() -> {
            log.warn("Failed to edit education: User with ID {} not found", authenticatedId);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        Education education = user.getEducation()
                .stream()
                .filter(e -> e.getId().equals(educationId))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Failed to edit education: Education with ID {} not found for user {}", educationId, authenticatedId);
                    return new ResourceNotFoundException("EDUCATION_NOT_FOUND");
                });

        educationMapper.mapForUpdate(education, educationRequest);

        userRepo.save(user);

        return educationMapper.toDto(education);
    }


    @Override
    @Transactional
    public void deleteEducation(Long id, Long educationId, HttpServletRequest request) {
        Long authenticatedId = authUtil.resolveAndAuthorizeUser(request, id);
        log.info("User with ID {} initiated deletion for education ID {}", authenticatedId, educationId);
        Education education = educationRepo.findByIdAndUserId(educationId, authenticatedId)
                .orElseThrow(() -> {
                    log.warn("Failed to delete education: Education with ID {} not found or unauthorized access by user with ID {}", educationId, authenticatedId);
                    return new ResourceNotFoundException("NOT_FOUND");
                });
        educationRepo.delete(education);
        log.info("Education with ID {} deleted successfully by user with ID {}", educationId, authenticatedId);
    }


    @Override
    public Page<ExperienceResponse> getAllExperience(Long id, Pageable pageable) {
        log.info("Getting all experience of the user with ID {} started", id);
        if (!userRepo.existsById(id)) {
            log.warn("User with id {} not found.", id);
            throw new ResourceNotFoundException("User not found.");
        }
        Page<Experience> experiences = experienceRepo.findAllByUserId(id, pageable);
        Page<ExperienceResponse> experienceResponse = experiences.map(experienceMapper::toDto);
        log.info("Experience records for user with id {} returned", id);
        return experienceResponse;
    }

    @Override
    @Transactional
    public ExperienceResponse addExperience(Long id, HttpServletRequest request, ExperienceRequest experienceRequest) {
        Long authenticatedId = authUtil.resolveAndAuthorizeUser(request, id);
        log.info("Operation of adding new experience to the user with ID {} started.", authenticatedId);
        User user = userRepo.findById(authenticatedId).orElseThrow(() -> {
            log.warn("Failed to add new experience: User with ID {} not found.", authenticatedId);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        Experience experience = experienceMapper.toEntity(experienceRequest);
        experience.setUser(user);
        experienceRepo.save(experience);
        log.info("New experience to user with ID {} added", authenticatedId);
        return experienceMapper.toDto(experience);
    }

    @Override
    @Transactional
    public ExperienceResponse editExperience(Long id, Long experienceId, HttpServletRequest request, ExperienceRequest experienceRequest) {
        Long authenticatedId = authUtil.resolveAndAuthorizeUser(request, id);
        log.info("Operation of editing experience with ID {} for user with ID {} started.", experienceId, authenticatedId);
        Experience experience = experienceRepo.findByIdAndUserId(experienceId, authenticatedId)
                .orElseThrow(() -> {
                    log.warn("Failed to edit experience: Experience with ID {} not found or unauthorized access by user with ID {}", experienceId, authenticatedId);
                    return new ResourceNotFoundException("EXPERIENCE_NOT_FOUND");
                });
        experienceMapper.mapToUpdate(experience, experienceRequest);
        experienceRepo.save(experience);
        log.info("The process of editing user's experience successfully ended.");
        return experienceMapper.toDto(experience);
    }

    @Override
    @Transactional
    public void deleteExperience(Long id, Long experienceId, HttpServletRequest request) {
        Long authenticatedId = authUtil.resolveAndAuthorizeUser(request, id);
        log.info("Operation of deleting experience of the user with ID {} started", authenticatedId);
        Experience experience = experienceRepo.findByIdAndUserId(experienceId, authenticatedId)
                .orElseThrow(() -> {
                    log.warn("Failed to delete experience: Experience with ID {} not found or unauthorized access by user with ID {}", experienceId, authenticatedId);
                    return new ResourceNotFoundException("EXPERIENCE_NOT_FOUND");
                });
        experienceRepo.delete(experience);
        log.info("Experience successfully deleted.");
    }

    @Override
    public Page<ProjectResponse> getAllProjects(Long id, Pageable pageable) {
        log.info("Operation of getting projects of the user with ID {} started...", id);
        if (!userRepo.existsById(id)) {
            log.warn("Failed to get projects: User with ID {} not found", id);
            throw new ResourceNotFoundException("USER_NOT_FOUND");
        }
        Page<Project> projects = projectRepo.findAllByUserId(id, pageable);
        Page<ProjectResponse> projectResponses = projects.map(projectMapper::toDto);
        log.info("Projects of the user with ID {} returned.", id);
        return projectResponses;
    }

    @Override
    @Transactional
    public ProjectResponse addProjects(Long id, HttpServletRequest request, ProjectRequest projectRequest) {
        Long authenticatedId = authUtil.resolveAndAuthorizeUser(request, id);
        log.info("Operation of adding new project for user with ID {} started...", authenticatedId);
        User user = userRepo.findById(authenticatedId).orElseThrow(() -> {
            log.warn("Failed to add new project: User with ID {} not found.", authenticatedId);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        Project project = projectMapper.toEntity(projectRequest);
        project.setUser(user);
        projectRepo.save(project);
        log.info("New project added for user with ID {}", authenticatedId);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional
    public ProjectResponse editProject(Long id, Long projectId, HttpServletRequest request, ProjectRequest projectRequest) {
        Long authenticatedId = authUtil.resolveAndAuthorizeUser(request, id);
        log.info("Process of editing project with ID {} for user with ID {} started....", projectId, authenticatedId);
        Project project = projectRepo.findByIdAndUserId(projectId, authenticatedId).orElseThrow(() -> {
            log.warn("Failed to edit project: Project with id {} not found or unauthorized access by user with ID {}", projectId, authenticatedId);
            return new ResourceNotFoundException("PROJECT_NOT_FOUND");
        });
        projectMapper.mapToUpdate(project, projectRequest);
        projectRepo.save(project);
        log.info("Project with id {} successfully updated.", projectId);
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional
    public void deleteProject(Long id, Long projectId, HttpServletRequest request) {
        Long authenticatedId = authUtil.resolveAndAuthorizeUser(request, id);
        log.info("Operation of deleting project with ID {} for user with ID {} started...", projectId, authenticatedId);
        Project project = projectRepo.findByIdAndUserId(projectId, authenticatedId).orElseThrow(() -> {
            log.warn("Failed to delete project: Project with id {} not found or unauthorized access by user with ID {}", projectId, authenticatedId);
            return new ResourceNotFoundException("PROJECT_NOT_FOUND");
        });
        projectRepo.delete(project);
        log.info("Project deleted successfully for user with ID {}", authenticatedId);
    }



   /* //admin operations
    @Override
    public List<UserResponse> getAll() {
        log.info("Attempting to get all users.");
        List<UserResponse> users = userRepo.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
        log.info("All users returned.");
        return users;
    }
*/

}
