package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.response.ReactionResponse;
import az.matrix.linkedinclone.enums.ReactionType;
import az.matrix.linkedinclone.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reactions")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;

    @GetMapping
    public Page<ReactionResponse> getReactions(@RequestParam(name = "targetId") Long targetId,
                                               @RequestParam(name = "reaction", required = false) ReactionType reactionType,
                                               @PageableDefault Pageable pageable) {
        return reactionService.getReactions(targetId, reactionType, pageable);
    }

    @PostMapping
    public ReactionResponse addReaction(@RequestParam(name = "targetId") Long targetId,
                                        @RequestParam("reaction") ReactionType reactionType) {
        return reactionService.addReaction(targetId, reactionType);
    }

    @PatchMapping("/{id}")
    public ReactionResponse updateReaction(@PathVariable Long id, @RequestParam("reaction") ReactionType reactionType) {
        return reactionService.updateReaction(id, reactionType);
    }
    @DeleteMapping("/{id}")
    public void deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id);
    }

}
