package az.matrix.linkedinclone.service.impl;

import az.matrix.linkedinclone.dto.request.CommentRequest;
import az.matrix.linkedinclone.dto.response.CommentResponse;
import az.matrix.linkedinclone.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Override
    public CommentResponse addComment(Long postId, CommentRequest comment) {
        return null;
    }
}
