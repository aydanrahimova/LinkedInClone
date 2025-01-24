package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.ProjectRequest;
import az.matrix.linkedinclone.dto.response.ProjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface ProjectService {
    Page<ProjectResponse> getProjectsByUserId(Long userId, Pageable pageable);

    ProjectResponse addProject(ProjectRequest projectRequest);

    ProjectResponse editProject(Long id, ProjectRequest projectRequest);

    void deleteProject(Long id);
}
