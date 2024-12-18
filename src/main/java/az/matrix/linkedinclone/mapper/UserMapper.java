package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.User;
import az.matrix.linkedinclone.dto.request.UserDetailsRequest;
import az.matrix.linkedinclone.dto.request.UserRequest;
import az.matrix.linkedinclone.dto.response.UserDetailsResponse;
import az.matrix.linkedinclone.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Value;

@Mapper(componentModel = "spring")
public interface UserMapper {



    UserDetailsResponse toDetailsResponse(User user);
    User toEntity(UserRequest requestDto);
    UserResponse toResponse(User user);
    void mapForUpdate(@MappingTarget User user, UserDetailsRequest userDetailsRequest);
}
