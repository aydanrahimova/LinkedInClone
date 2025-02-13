package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.UserRepository;
import az.matrix.linkedinclone.dto.request.*;
import az.matrix.linkedinclone.dto.response.UserDetailsResponse;
import az.matrix.linkedinclone.enums.EntityStatus;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.UserMapper;
import az.matrix.linkedinclone.service.UserService;
import az.matrix.linkedinclone.utility.AuthHelper;
import az.matrix.linkedinclone.utility.MediaUploadUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthHelper authHelper;
    private final PasswordEncoder passwordEncoder;

    @Value("${files.directory}")
    private String UPLOAD_DIR;

    @Override
    public UserDetailsResponse getById(Long id) {
        log.info("Attempting to get profile of user with ID {}", id);
        User user = userRepository.findByIdAndStatus(id, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(User.class));
        log.info("Successfully retrieved user with ID {}", id);
        return userMapper.toDetailsResponse(user);
    }

    @Override
    @Transactional
    public UserDetailsResponse editUserInfo(UserRequest userRequest) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of editing user information started by user with email {}", user.getEmail());
        userMapper.mapForUpdate(user, userRequest);
        userRepository.save(user);
        UserDetailsResponse detailsResponse = userMapper.toDetailsResponse(user);
        log.info("User with email {} successfully updated.", user.getEmail());
        return detailsResponse;
    }


    @Override
    @Transactional
    public void changePassword(ChangePasswordDto changePasswordDto) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of changing password started by user with email {}", user.getEmail());
        if (changePasswordDto.getNewPassword().equals(changePasswordDto.getRetryPassword()) &&
                passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(user);
            log.info("Password for user with email {} changed.", user.getEmail());
        } else {
            log.error("Failed to change password");
            throw new IllegalArgumentException("Old password entered incorrectly or new passwords do not match");
        }
    }

    @Override
    @Transactional
    public void deactivateUser(String password) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of deactivation user profile started by user with ID {}", user.getId());
        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setStatus(EntityStatus.DEACTIVATED);
            user.setDeactivationDate(LocalDateTime.now());
            user.setDeactivatedByAdmin(Boolean.FALSE);
            userRepository.save(user);
        } else {
            log.warn("Failed to deactivated user with ID {}: Incorrect password", user.getId());
            throw new IllegalArgumentException("INCORRECT_PASSWORD");
        }
    }

    @Override
    @Transactional
    public void deactivateUserByAdmin(Long userId) {
        User authenticatedAdmin = authHelper.getAuthenticatedUser();
        log.info("Operation of deactivating user with ID {} by admin with ID {} started", userId, authenticatedAdmin.getId());
        User user = userRepository.findByIdAndStatus(userId, EntityStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException(User.class));
        user.setStatus(EntityStatus.DEACTIVATED);
        user.setDeactivatedByAdmin(Boolean.TRUE);
        log.info("User with ID {} successfully deleted by admin with ID {}", userId, authenticatedAdmin.getId());
    }

    @Override
    @Transactional
    public void activateUserByAdmin(Long id) {
        User admin = authHelper.getAuthenticatedUser();
        log.info("Operation of activating user with ID {} started by admin with ID {}", id, admin.getId());
        User user = userRepository.findByIdAndStatus(id, EntityStatus.DEACTIVATED).orElseThrow(() -> new ResourceNotFoundException(User.class));
        user.setStatus(EntityStatus.ACTIVE);
        user.setDeactivationDate(null);
        userRepository.save(user);
        log.info("User with ID {} successfully activated by admin with ID {}", id, admin.getId());
    }

    @Override
    @Transactional
    public void changeProfilePhoto(MultipartFile photo) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of changing profile photo started by user with email {}", user.getEmail());
        if (user.getPhotoUrl() != null) {
            deleteExistingProfilePhoto();
        }
        String photoUrl = MediaUploadUtil.uploadPhoto(photo, UPLOAD_DIR);
        user.setPhotoUrl(photoUrl);
        userRepository.save(user);
        log.info("Profile photo for user with email {} changed successfully", user.getEmail());
    }

    @Override
    @Transactional
    public void deleteExistingProfilePhoto() {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of deletion existing profile photo started by user with email {}.", user.getEmail());
        if (user.getPhotoUrl() != null) {
            Path existingPhotoPath = Paths.get(user.getPhotoUrl());
            try {
                Files.deleteIfExists(existingPhotoPath);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to delete photo");
            }
        }
        user.setPhotoUrl(null);
        userRepository.save(user);
        log.info("Existing profile photo deleted successfully");
    }

    @Override
    @Transactional
    public void deleteUser() {
        log.info("Operation of deleting users started by system");
        List<User> users = userRepository.findAllByStatus(EntityStatus.DEACTIVATED);
        LocalDateTime threshold = LocalDateTime.now().minusDays(30).toLocalDate().atStartOfDay();
        for (User user : users) {
            if (user.getDeactivationDate().isBefore(threshold)) {
                user.setStatus(EntityStatus.DELETED);
                user.setDeactivationDate(null);
                userRepository.save(user);
            }
        }
        log.info("{} users successfully deleted", users.size());
    }

}
