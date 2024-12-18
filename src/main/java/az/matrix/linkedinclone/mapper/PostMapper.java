package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Post;
import az.matrix.linkedinclone.dto.request.PostRequest;
import az.matrix.linkedinclone.dto.response.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toEntity(PostRequest postRequest);

    PostResponse toDto(Post post);

    void mapForUpdate(@MappingTarget Post post, PostRequest postRequest);
}
