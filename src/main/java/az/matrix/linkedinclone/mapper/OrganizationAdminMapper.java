package az.matrix.linkedinclone.mapper;

import az.matrix.linkedinclone.dao.entity.OrganizationAdmin;
import az.matrix.linkedinclone.dto.response.OrganizationAdminResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationAdminMapper {
    OrganizationAdminResponse toDto(OrganizationAdmin organizationAdmin);
}
