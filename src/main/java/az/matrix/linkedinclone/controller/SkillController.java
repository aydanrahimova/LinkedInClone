package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.SkillRequest;
import az.matrix.linkedinclone.dto.response.SkillResponse;
import az.matrix.linkedinclone.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @GetMapping("/{id}")
    public ResponseEntity<SkillResponse> getPredefinedSkill(@PathVariable Long id) {
        SkillResponse skillResponse = skillService.getSkill(id);
        return ResponseEntity.ok(skillResponse);
    }

    @GetMapping
    public ResponseEntity<Page<SkillResponse>> getPredefinedSkills(@PageableDefault Pageable pageable) {
        Page<SkillResponse> skills = skillService.getPredefinedSkills(pageable);
        return ResponseEntity.ok(skills);
    }

    @PostMapping
    public ResponseEntity<SkillResponse> addPredefinedSkill(@Valid @RequestBody SkillRequest skillRequest) {
        SkillResponse skillResponse = skillService.addPredefinedSkill(skillRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(skillResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillResponse> editPredefinedSkill(@PathVariable Long id, @Valid @RequestBody SkillRequest skillRequest) {
        SkillResponse skillResponse = skillService.editPredefinedSkill(id, skillRequest);
        return ResponseEntity.ok(skillResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePredefinedSkill(@PathVariable Long id) {
        skillService.deletePredefinedSkill(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
