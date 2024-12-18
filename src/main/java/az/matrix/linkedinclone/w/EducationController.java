//package az.matrix.linkedinclone.controller;
//
//import az.matrix.linkedinclone.dto.EducationDto;
//import az.matrix.linkedinclone.service.EducationService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/education")
//@RequiredArgsConstructor
//public class EducationController {
//
//    private final EducationService educationService;
//
//    @Operation(summary = "Retrieves another user's education list")
//    @GetMapping("/users/{userId}")
//    public List<EducationDto> getAllEducationByUserId(@PathVariable Long userId) {
//        return educationService.getAllEducationByUserId(userId);
//    }
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public EducationDto addEducation(@Validated @RequestBody EducationDto dto) {
//        return educationService.addEducation(dto);
//    }
//
//    @PutMapping("/{eductionId}")
//    public EducationDto editEducationOfUser(@PathVariable Long eductionId, @Validated @RequestBody EducationDto educationDto) {
//        return educationService.editEducationOfUser(eductionId, educationDto);
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/{educationId}")
//    public void deleteEducationOfUser(@PathVariable Long educationId) {
//        educationService.deleteEducationOfUser(educationId);
//    }
//
//}
