package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Photo;
import az.matrix.linkedinclone.dao.entity.Post;
import az.matrix.linkedinclone.dto.request.PostRequest;
import az.matrix.linkedinclone.dto.response.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toEntity(PostRequest postRequest);

    @Mapping(target = "photosPath", source = "photos", qualifiedByName = "mapPhotosToPaths")
    PostResponse toDto(Post post);

    @Named("mapPhotosToPaths")
    default List<String> mapPhotosToPaths(List<Photo> photos) {
        if (photos == null || photos.isEmpty()) {
            return null;
        }
        return photos.stream()
                .map(Photo::getPhotoPath)
                .collect(Collectors.toList());
    }

    void mapForUpdate(@MappingTarget Post post, PostRequest postRequest);

}
