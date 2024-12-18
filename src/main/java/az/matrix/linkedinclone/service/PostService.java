package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.PostRequest;
import az.matrix.linkedinclone.dto.response.PostResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    PostResponse createPost(PostRequest post);

    PostResponse getPost(Long postId);

    PostResponse editPost(Long postId,PostRequest postRequest);

    void deletePost(Long postId);
}
