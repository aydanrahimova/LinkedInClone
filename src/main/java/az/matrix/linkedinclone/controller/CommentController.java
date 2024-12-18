package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.CommentRequest;
import az.matrix.linkedinclone.dto.response.CommentResponse;
import az.matrix.linkedinclone.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    public CommentResponse addComment(Long postId,@Valid @RequestBody CommentRequest comment){
        return commentService.addComment(postId,comment);
    }

}
