package az.matrix.linkedinclone.config.enums;

import lombok.Getter;

@Getter
public enum AuthUrlMapping {

    USER(Role.ROLE_USER.name(), new String[]{
            "/educations/users/{userId}",
            "/experiences/users/{userId}",
            "/projects/users/{userId}",
            "/skills/users/{userId}",
            "/users/{id}",
            "/comments",
            "/jobs",
            "/organizations/{id}",
            "/posts/**",
            "/reactions"
    }),
    ADMIN(Role.ROLE_ADMIN.name(), new String[]{
            "/users/admin/**",
            "/admin/**"
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
            "/educations",
            "/educations/{id}",
            "/experiences",
            "/experiences/{id}",
            "/projects",
            "/projects/{id}",
            "/skills",
            "/skills/{id}",
            "/users/**",
//            "/user/change-password",
//            "/user/deactivate",
            "/connections/all",
            "/comments",
            "/comments/**",
            "/connections/**",
            "/applications/**",
            "/jobs/**",
            "/organizations/**",
            "/posts/**",
            "/reactions/**"
    });


    private final String role;
    private final String[] urls;

    AuthUrlMapping(String role, String[] urls) {
        this.role = role;
        this.urls = urls;
    }

}

