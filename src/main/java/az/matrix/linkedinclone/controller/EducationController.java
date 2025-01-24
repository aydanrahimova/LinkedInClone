package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.EducationRequest;
import az.matrix.linkedinclone.dto.response.EducationResponse;
import az.matrix.linkedinclone.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EducationResponse addEducation(@Validated @RequestBody EducationRequest dto) {
        return educationService.addEducation(dto);
    }

    @PutMapping("/{id}")
    public EducationResponse editEducation(@PathVariable Long id, @Validated @RequestBody EducationRequest educationDto) {
        return educationService.editEducation(id, educationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEducation(@PathVariable Long id) {
        educationService.deleteEducation(id);
    }

}
