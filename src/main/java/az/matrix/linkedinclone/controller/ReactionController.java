package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.ReactionRequest;
import az.matrix.linkedinclone.dto.response.ReactionResponse;
import az.matrix.linkedinclone.enums.ReactionType;
import az.matrix.linkedinclone.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;

    @GetMapping
    public ResponseEntity<Page<ReactionResponse>> getReactions(@RequestParam(name = "targetId") Long targetId,
                                                               @RequestParam(name = "reaction", required = false) ReactionType reactionType,
                                                               @PageableDefault Pageable pageable) {
        Page<ReactionResponse> reactions = reactionService.getReactionsByTargetId(targetId, reactionType, pageable);
        return ResponseEntity.ok(reactions);
    }

    @PostMapping
    public ResponseEntity<ReactionResponse> addReaction(@RequestBody ReactionRequest reactionRequest) {
        ReactionResponse reactionResponse = reactionService.addReaction(reactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reactionResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReactionResponse> updateReaction(@PathVariable Long id, @RequestParam("reaction") ReactionType reactionType) {
        ReactionResponse reactionResponse = reactionService.updateReaction(id, reactionType);
        return ResponseEntity.ok(reactionResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
