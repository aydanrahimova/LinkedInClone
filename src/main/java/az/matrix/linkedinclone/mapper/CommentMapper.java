package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Comment;
import az.matrix.linkedinclone.dto.response.CommentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponse toDto(Comment comment);
}
