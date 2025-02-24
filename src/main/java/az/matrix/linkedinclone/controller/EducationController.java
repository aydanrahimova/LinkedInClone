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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/educations")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @GetMapping
    public ResponseEntity<Page<EducationResponse>> getEducationsByUserId(@RequestParam Long userId, @PageableDefault Pageable pageable) {
        Page<EducationResponse> responses = educationService.getEducationsByUserId(userId, pageable);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<EducationResponse> addEducation(@Valid @RequestBody EducationRequest educationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(educationService.addEducation(educationRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationResponse> editEducation(@PathVariable Long id, @Valid @RequestBody EducationRequest educationRequest) {
        EducationResponse response = educationService.editEducation(id, educationRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        educationService.deleteEducation(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
