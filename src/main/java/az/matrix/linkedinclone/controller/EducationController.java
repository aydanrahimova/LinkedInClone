package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.EducationRequest;
import az.matrix.linkedinclone.dto.response.EducationResponse;
import az.matrix.linkedinclone.service.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/educations")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @GetMapping
    public Page<EducationResponse> getEducationsByUserId(@RequestParam Long userId, @PageableDefault Pageable pageable) {
        return educationService.getEducationsByUserId(userId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EducationResponse addEducation(@Valid @RequestBody EducationRequest educationRequest) {
        return educationService.addEducation(educationRequest);
    }

    @PutMapping("/{id}")
    public EducationResponse editEducation(@PathVariable Long id, @Valid @RequestBody EducationRequest educationRequest) {
        return educationService.editEducation(id, educationRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEducation(@PathVariable Long id) {
        educationService.deleteEducation(id);
    }

}
