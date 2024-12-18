package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.PostRequest;
import az.matrix.linkedinclone.dto.response.PostResponse;
import az.matrix.linkedinclone.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{postId}")
    public PostResponse getPost(@PathVariable Long postId){
        return postService.getPost(postId);
    }

    @PostMapping
    public PostResponse createPost(@RequestBody PostRequest postRequest){
        return postService.createPost(postRequest);
    }

    @PutMapping("/{postId}")
    public PostResponse editPost(@PathVariable Long postId, @Valid @RequestBody PostRequest postRequest){
        return postService.editPost(postId,postRequest);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
    }

}
