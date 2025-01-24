package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.request.*;
import az.matrix.linkedinclone.dto.response.UserDetailsResponse;
import az.matrix.linkedinclone.dto.response.UserResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final AuthHelper authHelper;
    private final PasswordEncoder passwordEncoder;

    @Value("${files.directory}")
    private String UPLOAD_DIR;

    @Override
    public UserDetailsResponse getById(Long id) {
        log.info("Attempting to get profile of user with ID {}", id);
        User user = userRepo.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> {
                    log.warn("Failed to get user: User with ID {} not found or not active user", id);
                    return new ResourceNotFoundException("USER_NOT_FOUND_OR_NOT_ACTIVE");
                });
        log.info("Successfully retrieved user with ID {}", id);
        return userMapper.toDetailsResponse(user);
    }

    @Override
    @Transactional
    public UserDetailsResponse editUserInfo(UserDetailsRequest userDetailsRequest) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of editing user information started by user with email {}",user.getEmail());
        userMapper.mapForUpdate(user, userDetailsRequest);
        userRepo.save(user);
        log.info("User with email {} successfully updated.", user.getEmail());
        return userMapper.toDetailsResponse(user);
    }


    @Override
    @Transactional
    public void changePassword(ChangePasswordDto changePasswordDto) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of changing password started by user with email {}",user.getEmail());
        if (changePasswordDto.getNewPassword().equals(changePasswordDto.getRetryPassword()) &&
                passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepo.save(user);
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
        log.info("Operation of deactivation user profile started by user with email {}",user.getEmail());
        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setStatus(EntityStatus.DEACTIVATED);
            user.setDeactivationDate(LocalDate.now());
            userRepo.save(user);
        } else {
            log.warn("Failed to deactivated user with email {}: Incorrect password", user.getEmail());
            throw new IllegalArgumentException("INCORRECT_PASSWORD");
        }
    }

    @Override
    @Transactional
    public void deleteUser() {
        List<User> users = userRepo.findAllByStatus(EntityStatus.DEACTIVATED);
        LocalDate threshold = LocalDate.now().minusDays(30);
        for (User user : users) {
            if (user.getDeactivationDate().isBefore(threshold)) {
                user.setStatus(EntityStatus.DELETED);
                user.setDeactivationDate(null);
                userRepo.save(user);
            }
        }
    }

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public void deactivateUserByAdmin(Long userId) {

    }

    @Override
    @Transactional
    public void changeProfilePhoto(MultipartFile photo){
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of changing profile photo started by user with email {}",user.getEmail());
        if (user.getPhotoUrl() != null) {
            deleteExistingProfilePhoto();
        }
        String photoUrl = MediaUploadUtil.uploadPhoto(photo, UPLOAD_DIR);
        user.setPhotoUrl(photoUrl);
        userRepo.save(user);
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
                //deletes from file system
                Files.deleteIfExists(existingPhotoPath);
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to delete photo");
            }
        }
        user.setPhotoUrl(null);
        userRepo.save(user);
        log.info("Existing profile photo deleted successfully");
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
