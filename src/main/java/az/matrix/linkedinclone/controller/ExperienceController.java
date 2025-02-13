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
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/experiences")
@RequiredArgsConstructor
public class ExperienceController {
    private final ExperienceService experienceService;

    @GetMapping
    public Page<ExperienceResponse> getAllExperienceByUserId(@RequestParam Long userId, @PageableDefault Pageable pageable) {
        return experienceService.getExperiencesByUserId(userId, pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ExperienceResponse addExperience(@Valid @RequestBody ExperienceRequest experienceRequest) {
        return experienceService.addExperience(experienceRequest);
    }

    @PutMapping("/{id}")
    public ExperienceResponse editExperience(@PathVariable Long id, @Valid @RequestBody ExperienceRequest experienceRequest) {
        return experienceService.editExperience(id, experienceRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
    }

}
