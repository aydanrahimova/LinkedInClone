package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.SkillUser;
import az.matrix.linkedinclone.dto.request.SkillUserRequest;
import az.matrix.linkedinclone.dto.response.SkillUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SkillUserMapper {
    @Mapping(target = "skill.id", source = "skillId")
    SkillUser toEntity(SkillUserRequest skillUserRequest);

    SkillUserResponse toDto(SkillUser skillUser);

    void mapToUpdate(@MappingTarget SkillUser skillUser, SkillUserRequest skillUserRequest);
}
