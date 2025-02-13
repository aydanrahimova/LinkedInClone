package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Project;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.ProjectRepository;
import az.matrix.linkedinclone.dao.repo.UserRepository;
import az.matrix.linkedinclone.dto.request.ProjectRequest;
import az.matrix.linkedinclone.dto.response.ProjectResponse;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.ProjectMapper;
import az.matrix.linkedinclone.service.ProjectService;
import az.matrix.linkedinclone.utility.AuthHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final AuthHelper authHelper;

    @Override
    public Page<ProjectResponse> getProjectsByUserId(Long userId, Pageable pageable) {
        log.info("Getting all projects of the user with ID {} started", userId);
        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException(User.class);
        Page<Project> projects = projectRepository.findAllByUserId(userId, pageable);
        Page<ProjectResponse> projectResponsePage = projects.map(projectMapper::toDto);
        log.info("Projects of the user with ID {} returned.", userId);
        return projectResponsePage;
    }

    @Override
    @Transactional
    public ProjectResponse addProject(ProjectRequest projectDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Adding new project started by user with ID {}", authenticatedUser.getId());
        Project project = projectMapper.toEntity(projectDto);
        project.setUser(authenticatedUser);
        projectRepository.save(project);
        ProjectResponse response = projectMapper.toDto(project);
        log.info("New project added successfully");
        return response;
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Deleting project with ID {} for user with ID {} started", projectId, authenticatedUser.getId());
        Project project = projectRepository.findByIdAndUser(projectId, authenticatedUser).orElseThrow(() -> new ResourceNotFoundException(Project.class));
        projectRepository.delete(project);
        log.info("Project successfully deleted");
    }

    @Override
    @Transactional
    public ProjectResponse editProject(Long id, ProjectRequest projectDto) {
        User authenticatedUser = authHelper.getAuthenticatedUser();
        log.info("Editing project with ID {} for user with ID {} started....", id, authenticatedUser.getId());
        Project project = projectRepository.findByIdAndUser(id, authenticatedUser).orElseThrow(() -> new ResourceNotFoundException(Project.class));
        projectMapper.mapToUpdate(project, projectDto);
        projectRepository.save(project);
        ProjectResponse response = projectMapper.toDto(project);
        log.info("Project successfully updated.");
        return response;
    }

}
