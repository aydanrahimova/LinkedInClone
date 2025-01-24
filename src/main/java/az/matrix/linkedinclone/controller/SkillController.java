package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.SkillRequest;
import az.matrix.linkedinclone.dto.response.SkillResponse;
import az.matrix.linkedinclone.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @GetMapping("/{id}")
    public SkillResponse getPredefinedSkill(@PathVariable Long id) {
        return skillService.getSkill(id);
    }

    @GetMapping
    public Page<SkillResponse> getPredefinedSkills(@PageableDefault Pageable pageable) {
        return skillService.getPredefinedSkills(pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SkillResponse addPredefinedSkill(@Validated @RequestBody SkillRequest skillRequest) {
        return skillService.addPredefinedSkill(skillRequest);
    }

    @PutMapping("/{id}")
    public SkillResponse editPredefinedSkill(@PathVariable Long id, @Validated @RequestBody SkillRequest skillRequest) {
        return skillService.editPredefinedSkill(id, skillRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletePredefinedSkill(@PathVariable Long id) {
        skillService.deletePredefinedSkill(id);
    }
}
