package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.*;
import az.matrix.linkedinclone.dto.response.*;
import az.matrix.linkedinclone.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PutMapping
    public UserDetailsResponse editUserInfo(@Validated @RequestBody UserRequest userDetailsRequest) /*throws FileIOException, IOException*/ {
        return userService.editUserInfo(userDetailsRequest);
    }

    @PatchMapping(value = "/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void changeProfilePhoto(@RequestPart MultipartFile file) throws IOException {
        userService.changeProfilePhoto(file);
    }

    @PatchMapping("/deactivate")
    public void deactivateUser(@RequestBody String password) {
        userService.deactivateUser(password);
    }

    @PatchMapping("/password")
    public void changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(changePasswordDto);
    }

    @PatchMapping("/activate-by-admin/{id}")
    public void activateByAdmin(@PathVariable Long id) {
        userService.activateUserByAdmin(id);
    }

    @PatchMapping("/deactivate-by-admin/{id}")
    public void deactivateByAdmin(@PathVariable Long id) {
        userService.deactivateUserByAdmin(id);
    }

}
