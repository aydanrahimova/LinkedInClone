package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.*;
import az.matrix.linkedinclone.dto.response.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public interface UserService {

    UserDetailsResponse getById(Long id);

    UserDetailsResponse editUserInfo(UserDetailsRequest userDetailsRequest) /*throws FileIOException, IOException*/;

    void changePassword(ChangePasswordDto changePasswordDto);

    void deactivateUser(String password);

    void deleteUser();

    Page<UserResponse> getAll(Pageable pageable);

    void deactivateUserByAdmin(Long userId);


    void changeProfilePhoto(MultipartFile file) throws IOException;

    void deleteExistingProfilePhoto();
}
