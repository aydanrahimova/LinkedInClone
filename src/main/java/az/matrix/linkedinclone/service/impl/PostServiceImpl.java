package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Photo;
import az.matrix.linkedinclone.dao.entity.Post;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.PostRepository;
import az.matrix.linkedinclone.dto.request.PostRequest;
import az.matrix.linkedinclone.dto.response.AuthorResponse;
import az.matrix.linkedinclone.dto.response.PostResponse;
import az.matrix.linkedinclone.enums.AuthorType;
import az.matrix.linkedinclone.exception.ForbiddenException;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.PostMapper;
import az.matrix.linkedinclone.service.PostService;
import az.matrix.linkedinclone.utility.AuthHelper;
import az.matrix.linkedinclone.utility.AuthorHelper;
import az.matrix.linkedinclone.utility.MediaUploadUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final AuthHelper authHelper;
    private final AuthorHelper authorHelper;

    @Value("${files.directory}")
    private String UPLOAD_DIR;


    @Override
    public PostResponse getPost(Long postId) {
        log.info("Operation of getting post with ID {} started.", postId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(Post.class));
        PostResponse response = mapPostToResponse(post);
        log.info("Post with ID {} successfully returned", postId);
        return response;
    }

    @Override
    public Page<PostResponse> getPostsOfProfile(Long authorId, AuthorType authorType, Pageable pageable) {
        log.info("Operation of getting posts started");
        Page<Post> posts = postRepository.findAllByAuthorIdAndAuthorType(authorId, authorType, pageable);
        Page<PostResponse> responsePage = posts.map(this::mapPostToResponse);
        log.info("Posts are returned successfully");
        return responsePage;
    }

    @Override
    @Transactional
    public PostResponse createPost(PostRequest postRequest, List<MultipartFile> files) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Creating new post started");
        Post post = new Post();
        if (files != null && !files.isEmpty()) {
            handleMediaUpload(files, post);
        }
        Pair<Long, AuthorType> authorDetails = authorHelper.determineAuthorDetails(user, postRequest.getCompanyId());
        post.setAuthorId(authorDetails.getLeft());
        post.setAuthorType(authorDetails.getRight());
        post.setText(postRequest.getText());
        postRepository.save(post);
        PostResponse response = mapPostToResponse(post);
        log.info("Post successfully created");
        return response;
    }

    @Override
    @Transactional
    public PostResponse editPost(Long postId, PostRequest postRequest) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of editing post with id {} is started by user with email {}", postId, user.getEmail());
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(Post.class));
        if (!authorHelper.validatePermission(post.getAuthorType(), post.getAuthorId(), user)) {
            log.error("User with ID {} doesn't have permission to edit post with ID {}", user.getId(), postId);
            throw new ForbiddenException("NOT_ALLOWED");
        }
        postMapper.mapForUpdate(post, postRequest);
        postRepository.save(post);
        PostResponse response = mapPostToResponse(post);
        log.info("Post is successfully edited.");
        return response;
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of deleting post with id {} is started by user with email {}", postId, user.getEmail());
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException(Post.class));
        if (!authorHelper.validatePermission(post.getAuthorType(), post.getAuthorId(), user)) {
            log.error("User with ID {} doesn't have permission to delete post with ID {}", user.getId(), postId);
            throw new ForbiddenException("NOT_ALLOWED");
        }
        postRepository.delete(post);
        log.info("Post is successfully deleted.");
    }

    private void handleMediaUpload(List<MultipartFile> files, Post post) {
        for (MultipartFile file : files) {
            String fileUrl = MediaUploadUtil.uploadMediaForPost(file, UPLOAD_DIR);
            Photo postPhoto = Photo.builder().photoPath(fileUrl).post(post).build();
            post.getPhotos().add(postPhoto);
        }
    }

    private PostResponse mapPostToResponse(Post post) {
        PostResponse postResponse = postMapper.toDto(post);
        AuthorResponse authorResponse = authorHelper.getAuthor(post.getAuthorId(), post.getAuthorType());
        postResponse.setAuthor(authorResponse);
        return postResponse;
    }
}
