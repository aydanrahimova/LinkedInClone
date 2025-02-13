package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.ReactionRequest;
import az.matrix.linkedinclone.dto.response.ReactionResponse;
import az.matrix.linkedinclone.enums.ReactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ReactionService {
    Page<ReactionResponse> getReactionsByTargetId(Long targetId, ReactionType reactionType, Pageable pageable);

    ReactionResponse addReaction(ReactionRequest reactionRequest);

    ReactionResponse updateReaction(Long id, ReactionType reactionType);

    void deleteReaction(Long id);

}
