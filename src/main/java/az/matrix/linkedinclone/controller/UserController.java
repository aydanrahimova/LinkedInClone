package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.*;
import az.matrix.linkedinclone.dto.response.*;
import az.matrix.linkedinclone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDetailsResponse getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping(/*consumes = MediaType.MULTIPART_FORM_DATA_VALUE*/)
    public UserDetailsResponse editUserInfo(@Validated @RequestBody UserDetailsRequest userDetailsRequest) /*throws FileIOException, IOException*/ {
        return userService.editUserInfo(userDetailsRequest);
    }

    @PatchMapping(value = "/profile-photo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void changeProfilePhoto(@RequestPart MultipartFile file) throws IOException {
        userService.changeProfilePhoto(file);
    }

    @PatchMapping("/deactivate")
    public void deactivateUser(@RequestBody String password) {
        userService.deactivateUser(password);
    }

    @PatchMapping("/change-password")
    public void changePassword(@Validated @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(changePasswordDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/deactivate-user/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deactivateUserByAdmin(userId);
    }

    @GetMapping("/admin/users")
    public Page<UserResponse> getAllUser(@PageableDefault Pageable pageable) {
        return userService.getAll(pageable);
    }


//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/{id}")
//    public void deleteUser(@PathVariable Long id, HttpServletRequest request) {
//        userService.deleteUser(id, request);
//    }


    //proxy pattern-transactional
    //select for update
    //isolation level
    //propagation levels

}
