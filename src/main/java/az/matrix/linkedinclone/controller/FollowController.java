package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.response.UserResponse;
import az.matrix.linkedinclone.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getFollowers(@RequestParam Long organizationId, @PageableDefault Pageable pageable) {
        Page<UserResponse> followers = followService.getFollowers(organizationId, pageable);
        return ResponseEntity.ok(followers);
    }

    @PostMapping
    public ResponseEntity<Void> follow(@RequestParam Long organizationId) {
        followService.follow(organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unfollow(@RequestParam Long organizationId) {
        followService.unfollow(organizationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
