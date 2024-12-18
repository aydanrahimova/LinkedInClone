package az.matrix.linkedinclone.enums;

import java.util.Set;

public enum OrganizationRole {
    SUPER_ADMIN(Set.of(OrganizationPermission.EDIT_ORGANIZATION_PROFILE, OrganizationPermission.MANAGE_ROLES, OrganizationPermission.CONTENT_MANAGE)),
    CONTENT_ADMIN(Set.of(OrganizationPermission.CONTENT_MANAGE));

    private final Set<OrganizationPermission> permissions;

    OrganizationRole(Set<OrganizationPermission> permissions) {
        this.permissions = permissions;
    }

    public boolean hasPermission(OrganizationPermission permission) {
        return permissions.contains(permission);
    }
}
