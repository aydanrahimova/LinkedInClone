package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.response.ReactionResponse;
import az.matrix.linkedinclone.enums.ReactionTargetType;
import az.matrix.linkedinclone.enums.ReactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ReactionService {
    ReactionResponse addReaction(Long targetId,  ReactionType reactionType);

    void deleteReaction(Long id);

    Page<ReactionResponse> getReactions(Long targetId, ReactionType reactionType, Pageable pageable);

    ReactionResponse updateReaction(Long id, ReactionType reactionType);
}
