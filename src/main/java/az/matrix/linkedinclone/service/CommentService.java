package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.CommentRequest;
import az.matrix.linkedinclone.dto.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    Page<CommentResponse> getCommentsForPost(Long postId, Pageable pageable);

    CommentResponse addComment(Long postId, CommentRequest commentRequest);

    CommentResponse editComment(Long id,CommentRequest commentRequest);

    void deleteComment(Long id);

    CommentResponse replyToComment(Long id,CommentRequest commentRequest);
}
