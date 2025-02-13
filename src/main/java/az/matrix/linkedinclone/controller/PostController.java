package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.PostRequest;
import az.matrix.linkedinclone.dto.response.PostResponse;
import az.matrix.linkedinclone.enums.AuthorType;
import az.matrix.linkedinclone.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public Page<PostResponse> getPostsOfProfile(@RequestParam Long authorId, @RequestParam AuthorType authorType, @PageableDefault Pageable pageable) {
        return postService.getPostsOfProfile(authorId, authorType, pageable);
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createPost(@Valid @ModelAttribute PostRequest postRequest, @RequestPart(required = false) List<MultipartFile> files) throws IOException {
        return postService.createPost(postRequest, files);
    }

    @PutMapping("/{id}")
    public PostResponse editPost(@PathVariable Long id, @Valid @RequestBody PostRequest postRequest) {
        return postService.editPost(id, postRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

}
