package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Comment;
import az.matrix.linkedinclone.dao.entity.Post;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.CommentRepository;
import az.matrix.linkedinclone.dao.repo.PostRepository;
import az.matrix.linkedinclone.dto.request.CommentRequest;
import az.matrix.linkedinclone.dto.response.AuthorResponse;
import az.matrix.linkedinclone.dto.response.CommentResponse;
import az.matrix.linkedinclone.enums.AuthorType;
import az.matrix.linkedinclone.exception.ForbiddenException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.CommentMapper;
import az.matrix.linkedinclone.service.CommentService;
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
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final AuthHelper authHelper;
    private final AuthorHelper authorHelper;

    @Override
    public Page<CommentResponse> getCommentsForPost(Long postId, Pageable pageable) {
        log.info("Fetching comments for post with ID {} started", postId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(Post.class));
        Page<Comment> comments = commentRepository.findAllByPost(post, pageable);
        Page<CommentResponse> responses = comments.map(this::mapCommentToResponse);
        log.info("Fetched {} comments for post with ID {}", comments.getTotalElements(), postId);
        return responses;
    }

    @Override
    @Transactional
    public CommentResponse addComment(CommentRequest commentRequest) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Adding comment started");
        Post post = postRepository.findById(commentRequest.getPostId()).orElseThrow(() -> new ResourceNotFoundException(Post.class));
        Pair<Long, AuthorType> authorDetails = authorHelper.determineAuthorDetails(user, commentRequest.getCompanyId());
        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .post(post)
                .build();
        if (commentRequest.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(commentRequest.getParentCommentId()).orElseThrow(() -> new ResourceNotFoundException(Comment.class));
            comment.setParentComment(parentComment);
        }
        comment.setAuthorId(authorDetails.getLeft());
        comment.setAuthorType(authorDetails.getRight());
        commentRepository.save(comment);
        CommentResponse response = mapCommentToResponse(comment);
        log.info("Comment successfully added");
        return response;
    }

    @Override
    @Transactional
    public CommentResponse editComment(Long id, CommentRequest commentRequest) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Editing comment with ID {} started", id);
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Comment.class));
        if (!authorHelper.validatePermission(comment.getAuthorType(), comment.getAuthorId(), user))
            throw new ForbiddenException("NOT_ALLOWED");
        if (!comment.getContent().equals(commentRequest.getContent())) {
            comment.setContent(commentRequest.getContent());
            commentRepository.save(comment);
        }
        CommentResponse response = mapCommentToResponse(comment);
        log.info("Comment with ID {} successfully edited", id);
        return response;
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Deleting comment with ID {} started", id);
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Comment.class));
        if (!authorHelper.validatePermission(comment.getAuthorType(), comment.getAuthorId(), user) || !comment.getPost().getAuthorId().equals(user.getId())) {
            log.error("Failed to delete comment: User with email {} not allowed to delete comment", user.getEmail());
            throw new ForbiddenException("NOT_ALLOWED");
        }
        commentRepository.delete(comment);
        log.info("Comment with ID {} successfully deleted", id);
    }


    private CommentResponse mapCommentToResponse(Comment comment) {
        CommentResponse commentResponse = commentMapper.toDto(comment);
        AuthorResponse authorResponse = authorHelper.getAuthor(comment.getAuthorId(), comment.getAuthorType());
        commentResponse.setAuthor(authorResponse);
        return commentResponse;
    }

}
