package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.PostRequest;
import az.matrix.linkedinclone.dto.response.PostResponse;
import az.matrix.linkedinclone.service.PostService;
import jakarta.validation.Valid;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
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
    public Page<PostResponse> getPosts(@PageableDefault Pageable pageable){
        return postService.getPosts(pageable);
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostResponse createPost(@RequestParam String text,
                                   @RequestPart(required = false)List<MultipartFile> photos) throws IOException {
        return postService.createPost(text,photos);
    }

    @PutMapping("/{id}")
    public PostResponse editPost(@PathVariable Long id, @Valid @RequestBody PostRequest postRequest){
        return postService.editPost(id,postRequest);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id){
        postService.deletePost(id);
    }

}
