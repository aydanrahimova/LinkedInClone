package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.PostRequest;
import az.matrix.linkedinclone.dto.response.PostResponse;
import az.matrix.linkedinclone.enums.ReactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PostService {
    PostResponse createPost(String text, List<MultipartFile> photos) throws IOException;

    PostResponse getPost(Long postId);

    PostResponse editPost(Long postId,PostRequest postRequest);

    void deletePost(Long postId);

    Page<PostResponse> getPosts(Pageable pageable);

}
