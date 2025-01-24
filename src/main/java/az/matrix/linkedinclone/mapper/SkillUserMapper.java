package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.SkillUser;
import az.matrix.linkedinclone.dao.repo.SkillRepo;
import az.matrix.linkedinclone.dto.request.SkillUserRequest;
import az.matrix.linkedinclone.dto.response.SkillUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SkillUserMapper {
    SkillUser toEntity(SkillUserRequest skillUserRequest);
    SkillUserResponse toDto(SkillUser skillUser);
}
