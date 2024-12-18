//package az.matrix.linkedinclone.service.impl;
//
//import az.matrix.linkedinclone.dao.entity.Skill;
//import az.matrix.linkedinclone.dao.entity.User;
//import az.matrix.linkedinclone.dao.repo.SkillRepo;
//import az.matrix.linkedinclone.dao.repo.UserRepo;
//import az.matrix.linkedinclone.dto.SkillDto;
//import az.matrix.linkedinclone.exception.ResourceNotFoundException;
//import az.matrix.linkedinclone.mapper.SkillMapper;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//@Transactional
//public class SkillService {
//    private final SkillRepo skillRepo;
//    private final UserRepo userRepo;
//    private final SkillMapper skillMapper;
//
//
//    public SkillDto addSkill(SkillDto skillDto) {
//        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("Adding new skill for user {} started",currentUser);
//        User user = userRepo.findByEmail(currentUser).orElseThrow(() -> {
//            log.warn("User {} not found.", currentUser);
//            return new ResourceNotFoundException("USER_NOT_FOUND");
//        });
//        Skill skill = skillMapper.toEntity(skillDto);
////        skill.setUser(user);
//        skillRepo.save(skill);
//        log.info("New skill added for user {}",currentUser);
//        return skillMapper.toDto(skill);
//    }
//
//    public void deleteSkill(Long skillId) {
//        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("Deleting skill with id {} for user {} ...", skillId, currentUser);
//        Skill skill = skillRepo.findById(skillId).orElseThrow(() -> {
//            log.warn("Failed to delete skill: Skill with id {} not found.", skillId);
//            return new ResourceNotFoundException("SKILL_NOT_FOUND");
//        });
//        skillRepo.delete(skill);
//        log.info("Skill deleted successfully for user {}", currentUser);
//    }
//
//    public SkillDto editSkill(Long skillId, SkillDto skillDto) {
//        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("Editing skill with id {} for user {}...", skillId, currentUser);
//        Skill skill = skillRepo.findById(skillId).orElseThrow(() -> {
//            log.warn("Failed to edit skill: Skill with id {} not found.", skillId);
//            return new ResourceNotFoundException("SKILL_NOT_FOUND");
//        });
//        skillMapper.mapToUpdate(skill, skillDto);
//        skillRepo.save(skill);
//        log.info("Skill with id {} successfully updated.", skillId);
//        return skillMapper.toDto(skill);
//    }
//
//    public List<SkillDto> getAllSkillsByUserId(Long userId) {
//        log.info("Process of getting another user's skills stared");
//        if (!userRepo.existsById(userId)) {
//            log.warn("Failed to get skills: User with id {} not found.", userId);
//            throw new ResourceNotFoundException("User not found.");
//        }
//        List<SkillDto> skillDtoList = skillRepo.findAllByUserId(userId)
//                .stream()
//                .map(skillMapper::toDto)
//                .toList();
//        log.info("Skills for user with id {} returned", userId);
//        return skillDtoList;
//    }
//}
