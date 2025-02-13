package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.*;
import az.matrix.linkedinclone.dto.response.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public interface UserService {

    UserDetailsResponse getById(Long id);

    UserDetailsResponse editUserInfo(UserRequest userRequest);

    void changePassword(ChangePasswordDto changePasswordDto);

    void deactivateUser(String password);

    void activateUserByAdmin(Long id);

    void deactivateUserByAdmin(Long id);

    void deleteUser();

    void changeProfilePhoto(MultipartFile file) throws IOException;

    void deleteExistingProfilePhoto();

}
