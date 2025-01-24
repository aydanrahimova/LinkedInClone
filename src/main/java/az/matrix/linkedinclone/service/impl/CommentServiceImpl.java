package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Comment;
import az.matrix.linkedinclone.dao.entity.Post;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.CommentRepository;
import az.matrix.linkedinclone.dao.repo.PostRepository;
import az.matrix.linkedinclone.dto.request.CommentRequest;
import az.matrix.linkedinclone.dto.response.CommentResponse;
import az.matrix.linkedinclone.exception.ForbiddenException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.CommentMapper;
import az.matrix.linkedinclone.service.CommentService;
import az.matrix.linkedinclone.utility.AuthHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final AuthHelper authHelper;

    @Override
    public Page<CommentResponse> getCommentsForPost(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.error("Failed to fetch comments: Post with ID {} not found", postId);
                    return new ResourceNotFoundException("POST_NOT_FOUND");
                });
        Page<Comment> comments = commentRepository.findAllByPost(post, pageable);
        log.info("Fetched {} comments for post with ID {}", comments.getTotalElements(), postId);
        return comments.map(commentMapper::toDto);
    }

    @Override
    @Transactional
    public CommentResponse addComment(Long postId, CommentRequest commentRequest) {
        User user = authHelper.getAuthenticatedUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.error("Failed to add comment: Post with ID {} not found", postId);
                    return new ResourceNotFoundException("POST_NOT_FOUND");
                });

        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .post(post)
                .build();

        comment.setUser(user);

        commentRepository.save(comment);
        log.info("User with email {} successfully added a comment to post with ID {}", user.getEmail(), postId);
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public CommentResponse editComment(Long id, CommentRequest commentRequest) {
        User user = authHelper.getAuthenticatedUser();

        Comment comment = commentRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> {
                    log.error("Failed to edit comment: Comment with ID {} not found for user with email {}", id, user.getEmail());
                    return new ResourceNotFoundException("COMMENT_NOT_FOUND");
                });

        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);
        log.info("User with email {} successfully edited comment with ID {}", user.getEmail(), id);
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        User user = authHelper.getAuthenticatedUser();

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to delete comment: Comment with ID {} not.", id);
                    return new ResourceNotFoundException("COMMENT_NOT_FOUND");
                });

        if (!comment.getUser().equals(user) || !comment.getPost().getUser().equals(user)) {
            log.warn("Failed to delete comment with ID {}: You are not the author of comment or post", id);
            throw new ForbiddenException("NOT_ALLOWED");
        }

        commentRepository.delete(comment);
        log.info("User with email {} successfully deleted comment with ID {}", user.getEmail(), id);
    }

    @Override
    @Transactional
    public CommentResponse replyToComment(Long id, CommentRequest commentRequest) {
        User user = authHelper.getAuthenticatedUser();

        Comment parentComment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to reply to comment: Comment with ID {} not found", id);
                    return new ResourceNotFoundException("PARENT_COMMENT_NOT_FOUND");
                });

        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .post(parentComment.getPost())
                .parentComment(parentComment)
                .build();
        comment.setUser(user);


        commentRepository.save(comment);
        log.info("User with email {} successfully replied to comment with ID {}", user.getEmail(), id);
        return commentMapper.toDto(comment);
    }
}
