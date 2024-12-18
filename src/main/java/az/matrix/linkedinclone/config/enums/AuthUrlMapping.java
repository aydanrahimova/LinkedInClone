package az.matrix.linkedinclone.config.enums;

import lombok.Getter;

@Getter
public enum AuthUrlMapping {

    USER(Role.ROLE_USER.name(), new String[]{
            "/education/users/{userId}",
            "/experience/users/{userId}",
            "/projects/users/{userId}",
            "/skills/users/{userId}",
            "/user/{id}"
    }),
    ADMIN(Role.ROLE_ADMIN.name(), new String[]{
            "/user/admin/delete-user/{userId}",
            "/user/admin/users"

    }),
    PERMIT_ALL(null, new String[]{
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/swagger-ui/**",
            "/swagger-ui.html",



            "/auth/**"
    }),

    ANY_AUTHENTICATED(null, new String[]{
            "/education",
            "/education/{educationId}",
            "/experience",
            "/experience/{experienceId}",
            "/projects",
            "/projects/{projectId}",
            "/skills",
            "/skills/{skillId}",
            "/user/{id}/edit",
            "/user/{id}/change-password",
            "/user{id}/delete"
    });


    private final String role;
    private final String[] urls;

    AuthUrlMapping(String role, String[] urls) {
        this.role = role;
        this.urls = urls;
    }

}

