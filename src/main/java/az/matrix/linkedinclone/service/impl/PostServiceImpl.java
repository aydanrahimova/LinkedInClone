package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dao.entity.Photo;
import az.matrix.linkedinclone.dao.entity.Post;
import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dao.repo.PostRepository;
import az.matrix.linkedinclone.dto.request.PostRequest;
import az.matrix.linkedinclone.dto.response.PostResponse;
import az.matrix.linkedinclone.exception.ResourceNotFoundException;
import az.matrix.linkedinclone.mapper.PostMapper;
import az.matrix.linkedinclone.service.PostService;
import az.matrix.linkedinclone.utility.AuthHelper;
import az.matrix.linkedinclone.utility.MediaUploadUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final AuthHelper authHelper;

    @Value("${files.directory}")
    private String UPLOAD_DIR;


    @Override
    public PostResponse getPost(Long postId) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of getting post with ID {} started.", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.warn("Failed to get post: Post with ID {} not found", postId);
                    return new ResourceNotFoundException("POST_NOT_FOUND");
                });
        log.info("Post with ID {} successfully returned", postId);
        return postMapper.toDto(post);
    }

    @Override
    public Page<PostResponse> getPosts(Pageable pageable) {
        log.info("Operation of getting posts started");
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostResponse> postResponsePage = posts.map(postMapper::toDto);
        log.info("Posts are returned successfully");
        return postResponsePage;
    }

    @Override
    @Transactional
    public PostResponse createPost(String text, List<MultipartFile> files) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of creating new post started by user with email {}", user.getEmail());
        Post post = new Post();
        post.setText(text);
        post.setUser(user);
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileUrl = MediaUploadUtil.uploadMediaForPost(file, UPLOAD_DIR);
                Photo postPhoto = Photo.builder()
                        .photoPath(fileUrl)
                        .post(post)
                        .build();
                post.getPhotos().add(postPhoto);
            }
        }
        postRepository.save(post);
        log.info("Post is successfully created");
        return postMapper.toDto(post);
    }

    @Override
    @Transactional
    public PostResponse editPost(Long postId, PostRequest postRequest) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of editing post with id {} is started by user with email {}", postId, user.getEmail());
        Post post = postRepository.findByIdAndUserEmail(postId, user.getEmail())
                .orElseThrow(() -> {
                    log.warn("Failed to edit post: Post with ID {} not found or unauthorized access by user with email {}", postId, user.getEmail());
                    return new ResourceNotFoundException("POST_NOT_FOUND");
                });
        postMapper.mapForUpdate(post, postRequest);
        postRepository.save(post);
        log.info("Post is successfully edited.");
        return postMapper.toDto(post);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        User user = authHelper.getAuthenticatedUser();
        log.info("Operation of deleting post with id {} is started by user with email {}", postId, user.getEmail());
        Post post = postRepository.findByIdAndUserEmail(postId, user.getEmail())
                .orElseThrow(() -> {
                    log.warn("Failed to delete post: Post with ID {} not found or unauthorized access by user with email {}", postId, user.getEmail());
                    return new ResourceNotFoundException("POST_NOT_FOUND");
                });
        postRepository.delete(post);
        log.info("Post is successfully deleted.");
    }
}
