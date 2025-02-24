package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.SkillUserRequest;
import az.matrix.linkedinclone.dto.response.SkillUserResponse;
import az.matrix.linkedinclone.service.UserSkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-skills")
@RequiredArgsConstructor
public class UserSkillController {
    private final UserSkillService userSkillService;

    @GetMapping
    public ResponseEntity<Page<SkillUserResponse>> getSkillsByUserId(@RequestParam Long userId, @PageableDefault Pageable pageable) {
        Page<SkillUserResponse> skills = userSkillService.getSkillsByUserId(userId, pageable);
        return ResponseEntity.ok(skills);
    }

    @PostMapping
    public ResponseEntity<SkillUserResponse> addSkillToUser(@Valid @RequestBody SkillUserRequest skillUserRequest) {
        SkillUserResponse skill = userSkillService.addSkillToUser(skillUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(skill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillUserResponse> editSkillOfUser(@PathVariable Long id, @Valid @RequestBody SkillUserRequest skillUserRequest) {
        SkillUserResponse updatedSkill = userSkillService.editSkillOfUser(id, skillUserRequest);
        return ResponseEntity.ok(updatedSkill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkillOfUser(@PathVariable Long id) {
        userSkillService.deleteSkillOfUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
