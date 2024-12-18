//package az.matrix.linkedinclone.controller;
//
//import az.matrix.linkedinclone.dto.SkillDto;
//import az.matrix.linkedinclone.service.impl.SkillService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/skills")
//@RequiredArgsConstructor
//public class SkillController {
//
//    private final SkillService skillService;
//
//    @Operation(summary = "Retrieves another user's skills list")
//    @GetMapping("/users/{userId}")
//    public List<SkillDto> getAllSkillsByUserId(@PathVariable Long userId){
//        return skillService.getAllSkillsByUserId(userId);
//    }
//
////    @Operation(summary = "Retrieves the authenticated user's skills list")
////    @GetMapping()
////    public List<SkillDto> getOwnExperience(){
////        return skillService.getOwnSkills();
////    }
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public SkillDto addExperience(@Validated @RequestBody SkillDto skillDto){
//        return skillService.addSkill(skillDto);
//    }
//
//    @PutMapping("/{skillId}")
//    public SkillDto editExperience( @PathVariable Long skillId, @Validated @RequestBody SkillDto skillDto){
//        return skillService.editSkill( skillId,skillDto);
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/{skillId}")
//    public void deleteExperience(@PathVariable Long skillId){
//        skillService.deleteSkill(skillId);
//    }
//
//
//
//}
