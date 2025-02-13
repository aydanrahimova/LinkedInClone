package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Skill;
import az.matrix.linkedinclone.dto.request.SkillRequest;
import az.matrix.linkedinclone.dto.response.SkillResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring"/*,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE*/)
public interface SkillMapper{
    Skill toEntity(SkillRequest skillRequest);
    SkillResponse toDto(Skill skill);
    void mapToUpdate(@MappingTarget Skill skill, SkillRequest skillRequest);
}
