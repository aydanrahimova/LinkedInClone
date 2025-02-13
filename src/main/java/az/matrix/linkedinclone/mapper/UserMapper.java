package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dto.request.UserRequest;
import az.matrix.linkedinclone.dto.response.UserDetailsResponse;
import az.matrix.linkedinclone.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDetailsResponse toDetailsResponse(User user);

    UserResponse toDto(User user);

    User toEntity(UserRequest requestDto);

    void mapForUpdate(@MappingTarget User user, UserRequest userDetailsRequest);
}
