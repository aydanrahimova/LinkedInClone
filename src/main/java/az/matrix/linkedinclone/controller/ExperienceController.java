package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.ExperienceRequest;
import az.matrix.linkedinclone.dto.response.ExperienceResponse;
import az.matrix.linkedinclone.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping
    public ResponseEntity<Page<ExperienceResponse>> getAllExperienceByUserId(@RequestParam Long userId, @PageableDefault Pageable pageable) {
        Page<ExperienceResponse> experiences = experienceService.getExperiencesByUserId(userId, pageable);
        return ResponseEntity.ok(experiences);
    }

    @PostMapping
    public ResponseEntity<ExperienceResponse> addExperience(@Valid @RequestBody ExperienceRequest experienceRequest) {
        ExperienceResponse experienceResponse = experienceService.addExperience(experienceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(experienceResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponse> editExperience(@PathVariable Long id, @Valid @RequestBody ExperienceRequest experienceRequest) {
        ExperienceResponse experienceResponse = experienceService.editExperience(id, experienceRequest);
        return ResponseEntity.ok(experienceResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
