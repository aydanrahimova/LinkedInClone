package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Post;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.PostRepo;
import az.matrix.linkedinclone.dao.repo.UserRepo;
import az.matrix.linkedinclone.dto.request.PostRequest;
import az.matrix.linkedinclone.dto.response.PostResponse;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.PostMapper;
import az.matrix.linkedinclone.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final UserRepo userRepo;
    private final PostMapper postMapper;
    private final PostRepo postRepo;

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of creating a new post for user {} started", currentUserEmail);
        User user = userRepo.findByEmail(currentUserEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to create a new post: User {} not found", currentUserEmail);
                    return new ResourceNotFoundException("USER_NOT_FOUND");
                });
        Post post = postMapper.toEntity(postRequest);
        post.setUser(user);
        log.info("Post is successfully added");
        postRepo.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public PostResponse getPost(Long postId) {
        log.info("Operation of getting post with ID {} started.", postId);
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> {
                    log.warn("Failed to get post: Post with ID {} not found", postId);
                    return new ResourceNotFoundException("POST_NOT_FOUND");
                });
        log.info("Post with ID {} successfully returned", postId);
        return postMapper.toDto(post);
    }

    @Override
    public PostResponse editPost(Long postId, PostRequest postRequest) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of editing post with id {} is started by user {}", postId, currentUserEmail);
        Post post = postRepo.findByIdAndUserEmail(postId, currentUserEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to edit post: Post with ID {} not found or unauthorized access by user {}", postId, currentUserEmail);
                    return new ResourceNotFoundException("POST_NOT_FOUND");
                });
        postMapper.mapForUpdate(post, postRequest);
        postRepo.save(post);
        log.info("Post is successfully edited.");
        return postMapper.toDto(post);
    }

    @Override
    public void deletePost(Long postId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of deleting post with id {} is started by user {}", postId, currentUserEmail);
        Post post = postRepo.findByIdAndUserEmail(postId, currentUserEmail)
                .orElseThrow(() -> {
                    log.warn("Failed to delete post: Post with ID {} not found or unauthorized access by user {}", postId, currentUserEmail);
                    return new ResourceNotFoundException("POST_NOT_FOUND");
                });
        postRepo.delete(post);
        log.info("Post is successfully deleted.");
    }
}
