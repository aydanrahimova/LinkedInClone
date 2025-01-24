package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.SkillUserRequest;
import az.matrix.linkedinclone.dto.response.SkillUserResponse;
import az.matrix.linkedinclone.service.SkillService;
import az.matrix.linkedinclone.service.UserSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-skills")
@RequiredArgsConstructor
public class UserSkillController {
    private final UserSkillService userSkillService;

    @GetMapping
    public Page<SkillUserResponse> getSkillsOfUser(@RequestParam Long userId, @PageableDefault Pageable pageable){
        return userSkillService.getSkillsOfUser(userId,pageable);
    }

    @PostMapping
    public SkillUserResponse addSkillToUser(@Validated @RequestBody SkillUserRequest skillUserRequest){
        return userSkillService.addSkillToUser(skillUserRequest);
    }

    @PutMapping("/{id}")
    public SkillUserResponse editSkillOfUser(@PathVariable Long id,@Validated @RequestBody SkillUserRequest skillUserRequest){
        return userSkillService.editSkillOfUser(id,skillUserRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteSkillOfUser(@PathVariable Long id){
        userSkillService.deleteSkillOfUser(id);
    }
}
