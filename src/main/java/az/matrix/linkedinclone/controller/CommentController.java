package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.CommentRequest;
import az.matrix.linkedinclone.dto.response.CommentResponse;
import az.matrix.linkedinclone.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public Page<CommentResponse> getCommentsForPost(@RequestParam Long postId, @PageableDefault Pageable pageable) {
        return commentService.getCommentsForPost(postId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(@Valid @RequestBody CommentRequest commentRequest) {
        return commentService.addComment(commentRequest);
    }

    @PatchMapping("/{id}")
    public CommentResponse editComment(@PathVariable Long id, @Valid @RequestBody CommentRequest commentRequest) {
        return commentService.editComment(id, commentRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

}
