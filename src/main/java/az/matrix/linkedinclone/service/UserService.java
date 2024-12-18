package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.*;
import az.matrix.linkedinclone.dto.response.EducationResponse;
import az.matrix.linkedinclone.dto.response.ExperienceResponse;
import az.matrix.linkedinclone.dto.response.ProjectResponse;
import az.matrix.linkedinclone.dto.response.UserDetailsResponse;
import az.matrix.linkedinclone.exception.FileIOException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public interface UserService {

    UserDetailsResponse getById(Long id);
    UserDetailsResponse editUserInfo(UserDetailsRequest userDetailsRequest) throws FileIOException, IOException;
    void changePassword(ChangePasswordDto changePasswordDto);
    void deactivateUser(String password);
    void deleteUser();

    EducationResponse addEducation(Long id, HttpServletRequest request, EducationRequest educationDto);
    EducationResponse editEducation(Long id, Long educationId, HttpServletRequest request,EducationRequest educationRequest);
    Page<EducationResponse> getAllEducation(Long id, Pageable pageable);

    void deleteEducation(Long id, Long educationId, HttpServletRequest request);

    Page<ExperienceResponse> getAllExperience(Long id,Pageable pageable);
    ExperienceResponse addExperience(Long id, HttpServletRequest request, ExperienceRequest experienceRequest);
    ExperienceResponse editExperience(Long id, Long experienceId, HttpServletRequest request,ExperienceRequest experienceRequest);
    void deleteExperience(Long id, Long experienceId, HttpServletRequest request);

    Page<ProjectResponse> getAllProjects(Long id,Pageable pageable);
    ProjectResponse addProjects(Long id, HttpServletRequest request, ProjectRequest projectRequest);
    ProjectResponse editProject(Long id, Long projectId, HttpServletRequest request,ProjectRequest projectRequest);
    void deleteProject(Long id, Long projectId, HttpServletRequest request);


}
