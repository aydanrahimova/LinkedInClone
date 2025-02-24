package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.ProjectRequest;
import az.matrix.linkedinclone.dto.response.ProjectResponse;
import az.matrix.linkedinclone.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getProjectsByUserId(@RequestParam Long userId, @PageableDefault Pageable pageable) {
        Page<ProjectResponse> projects = projectService.getProjectsByUserId(userId, pageable);
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> addProject(@Valid @RequestBody ProjectRequest projectRequest) {
        ProjectResponse projectResponse = projectService.addProject(projectRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> editProject(@PathVariable Long id, @Valid @RequestBody ProjectRequest projectRequest) {
        ProjectResponse projectResponse = projectService.editProject(id, projectRequest);
        return ResponseEntity.ok(projectResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

