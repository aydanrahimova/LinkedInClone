package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.PostRequest;
import az.matrix.linkedinclone.dto.response.PostResponse;
import az.matrix.linkedinclone.enums.AuthorType;
import az.matrix.linkedinclone.enums.ReactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface PostService {

    Page<PostResponse> getPostsOfProfile(Long authorId, AuthorType authorType, Pageable pageable);

    PostResponse getPost(Long postId);

    PostResponse createPost(PostRequest postRequest,List<MultipartFile> files) throws IOException;

    PostResponse editPost(Long postId, PostRequest postRequest);

    void deletePost(Long postId);


}
