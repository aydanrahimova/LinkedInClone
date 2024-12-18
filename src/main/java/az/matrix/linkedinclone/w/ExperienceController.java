//package az.matrix.linkedinclone.controller;
//
//import az.matrix.linkedinclone.dto.ExperienceDto;
//import az.matrix.linkedinclone.service.ExperienceService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/experience")
//@RequiredArgsConstructor
//public class ExperienceController {
//    private final ExperienceService experienceService;
//
//    @GetMapping("/users/{userId}")
//    public List<ExperienceDto> getAllExperienceByUserId(@PathVariable Long userId){
//        return experienceService.getAllExperienceByUserId(userId);
//    }
//
////    @Operation(summary = "Retrieves the authenticated user's experience list")
////    @GetMapping("/me")
////    public List<ExperienceDto> getOwnExperience(){
////        return experienceService.getExperience();
////    }
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public ExperienceDto addExperience(@Validated @RequestBody ExperienceDto experienceDto){
//        return experienceService.addExperience(experienceDto);
//    }
//
//    @PutMapping("/{experienceId}")
//    public ExperienceDto editExperience(@PathVariable Long experienceId,@Validated @RequestBody ExperienceDto experienceDto){
//        return experienceService.editExperience(experienceId,experienceDto);
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/{experienceId}")
//    public void deleteExperience(@PathVariable Long experienceId){
//        experienceService.deleteExperience(experienceId);
//    }
//
//
//}
