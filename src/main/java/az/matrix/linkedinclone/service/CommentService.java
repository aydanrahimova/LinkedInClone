package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.CommentRequest;
import az.matrix.linkedinclone.dto.response.CommentResponse;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    CommentResponse addComment(Long postId, CommentRequest comment);
}
