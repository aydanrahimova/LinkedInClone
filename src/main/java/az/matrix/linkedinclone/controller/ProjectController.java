package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.ProjectRequest;
import az.matrix.linkedinclone.dto.response.ProjectResponse;
import az.matrix.linkedinclone.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public Page<ProjectResponse> getProjectsByUserId(@RequestParam Long userId, @PageableDefault Pageable pageable) {
        return projectService.getProjectsByUserId(userId, pageable);
    }

    @PostMapping
    public ProjectResponse addProject(@Validated @RequestBody ProjectRequest projectRequest) {
        return projectService.addProject(projectRequest);
    }

    @PutMapping("/{id}")
    public ProjectResponse editProject(@PathVariable Long id, @Validated @RequestBody ProjectRequest projectRequest) {
        return projectService.editProject(id, projectRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

}

