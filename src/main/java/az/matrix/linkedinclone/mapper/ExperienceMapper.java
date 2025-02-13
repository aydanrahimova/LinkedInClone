package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Experience;
import az.matrix.linkedinclone.dto.request.ExperienceRequest;
import az.matrix.linkedinclone.dto.response.ExperienceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring"/*,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE*/)
public interface ExperienceMapper {
    Experience toEntity(ExperienceRequest experienceRequest);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    ExperienceResponse toDto(Experience entity);

    void mapToUpdate(@MappingTarget Experience experience, ExperienceRequest experienceRequest);
}
