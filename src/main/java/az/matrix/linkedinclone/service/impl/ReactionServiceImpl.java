package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Reaction;
import az.matrix.linkedinclone.dao.entity.ReactionTarget;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.ReactionRepository;
import az.matrix.linkedinclone.dao.repo.ReactionTargetRepository;
import az.matrix.linkedinclone.dto.request.ReactionRequest;
import az.matrix.linkedinclone.dto.response.AuthorResponse;
import az.matrix.linkedinclone.dto.response.ReactionResponse;
import az.matrix.linkedinclone.enums.AuthorType;
import az.matrix.linkedinclone.enums.ReactionType;
import az.matrix.linkedinclone.exception.AlreadyExistException;
import az.matrix.linkedinclone.exception.ForbiddenException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.ReactionMapper;
import az.matrix.linkedinclone.service.ReactionService;
import az.matrix.linkedinclone.utility.AuthHelper;
import az.matrix.linkedinclone.utility.AuthorHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final ReactionMapper reactionMapper;
    private final ReactionTargetRepository reactionTargetRepository;
    private final AuthHelper authHelper;
    private final AuthorHelper authorHelper;

    @Override
    public Page<ReactionResponse> getReactionsByTargetId(Long targetId, ReactionType reactionType, Pageable pageable) {
        ReactionTarget target = reactionTargetRepository.findById(targetId).orElseThrow();
        Page<Reaction> reactions = reactionType != null ? reactionRepository.findAllByTargetAndReactionType(target, reactionType, pageable) : reactionRepository.findAllByTarget(target, pageable);
        return reactions.map(this::mapReactionToResponse);
    }

    @Override
    @Transactional
    public ReactionResponse addReaction(ReactionRequest reactionRequest) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of adding reaction to target with ID {} started", reactionRequest.getTargetId());
        ReactionTarget target = reactionTargetRepository.findById(reactionRequest.getTargetId()).orElseThrow(() -> new ResourceNotFoundException(ReactionTarget.class));
        Pair<Long, AuthorType> authorDetails = authorHelper.determineAuthorDetails(user, reactionRequest.getCompanyId());
        if (!authorHelper.validatePermission(authorDetails.getRight(), authorDetails.getLeft(), user))
            throw new ForbiddenException("NOT_ALLOWED");
        if (reactionRepository.existsByTargetAndAuthorIdAndAuthorType(target, authorDetails.getLeft(), authorDetails.getRight())) {
            log.error("Failed to react:{} with ID {} already reacted to this target with ID {}", authorDetails.getRight(), authorDetails.getLeft(), target.getId());
            throw new AlreadyExistException(Reaction.class);
        }
        Reaction reaction = Reaction.builder()
                .authorType(authorDetails.getRight())
                .authorId(authorDetails.getLeft())
                .target(target)
                .reactionType(reactionRequest.getReactionType())
                .build();
        reactionRepository.save(reaction);
        ReactionResponse response = mapReactionToResponse(reaction);
        log.info("Reaction {} was successfully added to target with ID {} ", reactionRequest.getReactionType(), reactionRequest.getTargetId());
        return response;
    }

    @Override
    @Transactional
    public ReactionResponse updateReaction(Long id, ReactionType reactionType) {
        User user = authHelper.getAuthenticatedUser();
        Reaction reaction = reactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Reaction.class));
        if (!authorHelper.validatePermission(reaction.getAuthorType(), reaction.getAuthorId(), user))
            throw new ForbiddenException("NOT_ALLOWED");
        if (!reactionType.equals(reaction.getReactionType())) {
            reaction.setReactionType(reactionType);
            reactionRepository.save(reaction);
        }
        ReactionResponse response = mapReactionToResponse(reaction);
        log.info("Reaction with ID {} was successfully updated to {}", id, reaction);
        return response;
    }

    @Override
    @Transactional
    public void deleteReaction(Long id) {
        User user = authHelper.getAuthenticatedUser();
        Reaction reaction = reactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Reaction.class));
        if (!authorHelper.validatePermission(reaction.getAuthorType(), reaction.getAuthorId(), user))
            throw new ForbiddenException("NOT_ALLOWED");
        reactionRepository.delete(reaction);
        log.info("Reaction successfully deleted.");
    }

    private ReactionResponse mapReactionToResponse(Reaction reaction) {
        ReactionResponse reactionResponse = reactionMapper.toDto(reaction);
        AuthorResponse authorResponse = authorHelper.getAuthor(reaction.getAuthorId(), reaction.getAuthorType());
        reactionResponse.setAuthor(authorResponse);
        return reactionResponse;
    }
}
