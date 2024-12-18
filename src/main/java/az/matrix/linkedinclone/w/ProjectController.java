//
//package az.matrix.linkedinclone.controller;
//
//import az.matrix.linkedinclone.dto.ProjectDto;
//import az.matrix.linkedinclone.service.impl.ProjectService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/projects")
//@RequiredArgsConstructor
//public class ProjectController {
//    private final ProjectService projectService;
//
//    @Operation(summary = "Retrieves another user's project list")
//    @GetMapping("/users/{userId}")
//    public List<ProjectDto> getProjectsByUserId(@PathVariable Long userId) {
//        return projectService.getAllProjectsByUserId(userId);
//    }
//
////    @Operation(summary = "Retrieves the authenticated user's project list")
////    @GetMapping("/me")
////    public List<ProjectDto> getOwnProjects() {
////        return projectService.getOwnProjects();
////    }
//
//    @PostMapping
//    public ProjectDto addProject(@Validated @RequestBody ProjectDto project) {
//        return projectService.addProject(project);
//    }
//
//    @PutMapping("/{projectId}")
//    public ProjectDto editProject(@PathVariable Long projectId, @Validated @RequestBody ProjectDto project) {
//        return projectService.editProject(projectId, project);
//    }
//
//    @DeleteMapping("/{projectId}")
//    public void deleteProject(@PathVariable Long projectId) {
//        projectService.deleteProject(projectId);
//    }
//}
//
