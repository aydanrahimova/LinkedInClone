package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Project;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.ProjectRepo;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.request.ProjectRequest;
import az.matrix.linkedinclone.dto.response.ProjectResponse;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.ProjectMapper;
import az.matrix.linkedinclone.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final UserRepo userRepo;
    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;

    public Page<ProjectResponse> getProjectsByUserId(Long userId, Pageable pageable) {
        log.info("Operation of getting projects of the user with id {} started...", userId);
        if (!userRepo.existsById(userId)) {
            log.warn("Failed to get projects: User with id {} not found", userId);
            throw new ResourceNotFoundException("USER_NOT_FOUND");
        }
        Page<ProjectResponse> projectDtoList = projectRepo.findAllByUserId(userId,pageable)
                .map(projectMapper::toDto) ;
        log.info("Projects of the user with id {} returned.", userId);
        return projectDtoList;
    }

    public ProjectResponse addProject(ProjectRequest projectDto) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of adding new project for user {} started...", currentUser);
        User user = userRepo.findByEmail(currentUser).orElseThrow(() -> {
            log.warn("User {} not found.", currentUser);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        Project project = projectMapper.toEntity(projectDto);
        project.setUser(user);
        projectRepo.save(project);
        log.info("New project added for user {}", currentUser);
        return projectMapper.toDto(project);
    }

    public void deleteProject(Long projectId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of deleting project with id {} for user {} started...", projectId, currentUser);
        Project project = projectRepo.findById(projectId).orElseThrow(() -> {
            log.warn("Failed to delete project: Project with id {} not found.", projectId);
            return new ResourceNotFoundException("PROJECT_NOT_FOUND");
        });
        projectRepo.delete(project);
        log.info("Project deleted successfully for user {}", currentUser);
    }

    public ProjectResponse editProject(Long id, ProjectRequest projectDto) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Process of editing project with id {} for user {} started....", id, currentUser);
        Project project = projectRepo.findById(id).orElseThrow(() -> {
            log.warn("Failed to edit project: Project with id {} not found.", id);
            return new ResourceNotFoundException("PROJECT_NOT_FOUND");
        });
        projectMapper.mapToUpdate(project, projectDto);
        projectRepo.save(project);
        log.info("Project with id {} successfully updated.", id);
        return projectMapper.toDto(project);
    }

}
