package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.*;
import az.matrix.linkedinclone.dto.response.EducationResponse;
import az.matrix.linkedinclone.dto.response.ExperienceResponse;
import az.matrix.linkedinclone.dto.response.ProjectResponse;
import az.matrix.linkedinclone.dto.response.UserDetailsResponse;
import az.matrix.linkedinclone.exception.FileIOException;
import az.matrix.linkedinclone.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDetailsResponse editUserInfo(@Validated @RequestBody UserDetailsRequest userDetailsRequest) throws FileIOException, IOException {
        return userService.editUserInfo(userDetailsRequest);
    }

//    @PatchMapping(value = "/change-profile-photo",)
//    public void changeProfilePhoto(@RequestPart MultipartFile file){
//
//    }

    @PatchMapping("/deactivate-user")
    public void deactivateUser(@RequestBody String password) {
        userService.deactivateUser(password);
    }

    @PatchMapping("/change-password")
    public void changePassword(@Validated @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(changePasswordDto);
    }

//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/{id}")
//    public void deleteUser(@PathVariable Long id, HttpServletRequest request) {
//        userService.deleteUser(id, request);
//    }

    @GetMapping("/{id}/education")
    public Page<EducationResponse> getAllEducation(@PathVariable Long id, @PageableDefault Pageable pageable) {
        return userService.getAllEducation(id, pageable);
    }

    @PostMapping("/{id}/education")
    public EducationResponse addEducation(
            @PathVariable Long id,
            HttpServletRequest request,
            @Validated @RequestBody EducationRequest educationDto
    ) {
        return userService.addEducation(id, request, educationDto);
    }

    @PutMapping("/{id}/education/{educationId}")
    public EducationResponse editEducation(
            @PathVariable Long id,
            @PathVariable Long educationId,
            HttpServletRequest request,
            @Validated @RequestBody EducationRequest educationRequest
    ) {
        return userService.editEducation(id, educationId, request, educationRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/education/{educationId}")
    public void deleteEducation(
            @PathVariable Long id,
            @PathVariable Long educationId,
            HttpServletRequest request
    ) {
        userService.deleteEducation(id, educationId, request);
    }

    @GetMapping("/{id}/experience")
    public Page<ExperienceResponse> getAllExperience(@PathVariable Long id, @PageableDefault Pageable pageable) {
        return userService.getAllExperience(id, pageable);
    }

    @PostMapping("/{id}/experience")
    public ExperienceResponse addExperience(
            @PathVariable Long id,
            HttpServletRequest request,
            @Validated @RequestBody ExperienceRequest experienceRequest
    ) {
        return userService.addExperience(id, request, experienceRequest);
    }

    @PutMapping("/{id}/experience/{experienceId}")
    public ExperienceResponse editExperience(
            @PathVariable Long id,
            @PathVariable Long experienceId,
            HttpServletRequest request,
            @Validated @RequestBody ExperienceRequest experienceRequest
    ) {
        return userService.editExperience(id, experienceId, request, experienceRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/experience/{experienceId}")
    public void deleteExperience(
            @PathVariable Long id,
            @PathVariable Long experienceId,
            HttpServletRequest request
    ) {
        userService.deleteExperience(id, experienceId, request);
    }

    @GetMapping("/{id}/project")
    public Page<ProjectResponse> getAllProjects(@PathVariable Long id, @PageableDefault Pageable pageable) {
        return userService.getAllProjects(id, pageable);
    }

    @PostMapping("/{id}/project")
    public ProjectResponse addProject(
            @PathVariable Long id,
            HttpServletRequest request,
            @Validated @RequestBody ProjectRequest projectResponse
    ) {
        return userService.addProjects(id, request, projectResponse);
    }

    @PutMapping("/{id}/project/{projectId}")
    public ProjectResponse editProject(
            @PathVariable Long id,
            @PathVariable Long projectId,
            HttpServletRequest request,
            @Validated @RequestBody ProjectRequest projectRequest
    ) {
        return userService.editProject(id, projectId, request, projectRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/project/{projectId}")
    public void deleteProject(
            @PathVariable Long id,
            @PathVariable Long projectId,
            HttpServletRequest request
    ) {
        userService.deleteProject(id, projectId, request);
    }


//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/admin/delete-user/{userId}")
//    public void deleteUser(@PathVariable Long userId){
//        userService.deleteUser(userId);
//    }

//    @GetMapping("/admin/users")
//    public List<UserResponse> getAllUser() {
//        return userService.getAll();
//    }


    //education falan userin icinde yaz
    //proxy pattern-transactional
    //select for update
    //isolation level
    //propagation levels

}
