package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.*;
import az.matrix.linkedinclone.dao.repo.*;
import az.matrix.linkedinclone.dto.response.ReactionResponse;
import az.matrix.linkedinclone.enums.ReactionType;
import az.matrix.linkedinclone.exception.AlreadyExistException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.ReactionMapper;
import az.matrix.linkedinclone.service.ReactionService;
import az.matrix.linkedinclone.utility.AuthHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepo reactionRepo;
    private final UserRepo userRepo;
    private final ReactionMapper reactionMapper;
    private final ReactionTargetRepository reactionTargetRepository;
    private final AuthHelper authHelper;

    @Override
    public Page<ReactionResponse> getReactions(Long targetId, ReactionType reactionType, Pageable pageable) {
        ReactionTarget target = reactionTargetRepository.findById(targetId).orElseThrow();
        Page<Reaction> reactions = reactionType != null ? reactionRepo.findAllByTargetAndReactionType(target, reactionType, pageable) : reactionRepo.findAllByTarget(target, pageable);
        return reactions.map(reactionMapper::toDto);
    }

    @Override
    @Transactional
    public ReactionResponse addReaction(Long targetId, ReactionType reactionType) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user1 = authHelper.getAuthenticatedUser();
        User user = userRepo.findByEmail(authenticatedUserEmail).orElseThrow(() ->  new ResourceNotFoundException(User.class));
        ReactionTarget target = reactionTargetRepository.findById(targetId).orElseThrow(() -> new ResourceNotFoundException(ReactionTarget.class));
        if (reactionRepo.existsByUserAndTarget(user, target)) {
            log.warn("Failed to add reaction: Reaction already exists");
            throw new AlreadyExistException("REACTION_ALREADY_EXISTS");
        }
        Reaction reaction = Reaction.builder()
                .user(user)
                .target(target)
                .reactionType(reactionType)
                .build();
        reactionRepo.save(reaction);
        log.info("Reaction {} was successfully added to target with ID {} by user {}",
                reactionType, targetId, authenticatedUserEmail);
        return reactionMapper.toDto(reaction);
    }


    @Override
    @Transactional
    public void deleteReaction(Long id) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(authenticatedUserEmail).orElseThrow();
        Reaction reaction = reactionRepo.findByIdAndUser(id, user).orElseThrow(() -> {
            log.warn("Failed to delete reaction: Reaction not found.");
            return new ResourceNotFoundException("NOT_FOUND");
        });
        reactionRepo.delete(reaction);
        log.info("Reaction successfully deleted.");
    }

    @Override
    @Transactional
    public ReactionResponse updateReaction(Long id, ReactionType reactionType) {
        String authenticatedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(authenticatedUserEmail).orElseThrow(() -> {
            log.warn("Failed to update reaction: User {} not found", authenticatedUserEmail);
            return new ResourceNotFoundException("NOT_FOUND");
        });
        Reaction reaction = reactionRepo.findByIdAndUser(id, user).orElseThrow(() -> {
            log.warn("Failed to update reaction: Reaction with ID {} not found or not found by user {}", id, authenticatedUserEmail);
            return new ResourceNotFoundException("NOT_FOUND");
        });
        reaction.setReactionType(reactionType);
        reactionRepo.save(reaction);
        log.info("Reaction with ID {} was successfully updated to {}", id, reaction);
        return reactionMapper.toDto(reaction);
    }
}
