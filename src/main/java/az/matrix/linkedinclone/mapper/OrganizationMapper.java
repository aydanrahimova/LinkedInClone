package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.Organization;
import az.matrix.linkedinclone.dto.request.OrganizationRequest;
import az.matrix.linkedinclone.dto.response.OrganizationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    Organization toEntity(OrganizationRequest organizationRequest);

    OrganizationResponse toDto(Organization organization);

//    @Mapping(target = "logoPath", source = "logoPath", ignore = true)
    void mapForUpdate(Organization organization, @MappingTarget OrganizationRequest organizationRequest);
}
