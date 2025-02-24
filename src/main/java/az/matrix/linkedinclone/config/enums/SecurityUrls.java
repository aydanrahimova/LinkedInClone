package az.matrix.linkedinclone.config.enums;

import lombok.Getter;

@Getter
public enum SecurityUrls {

    //GET methods
    USER_ADMIN(new String[]{Role.ROLE_USER.name(), Role.ROLE_ADMIN.name()}, new String[]{
            "/educations",
            "/experiences",
            "/projects",
            "/posts/**",
            "/skills/**",
            "/users/**",
            "/comments",
            "/jobs/**",
            "/skills/**",
            "/organizations/**",
            "/posts/**",
            "/reactions",
            "/user-skills"
    }),
    ADMIN(new String[]{Role.ROLE_ADMIN.name()}, new String[]{
            "users/activate-by-admin/{id}",
            "users/deactivate-by-admin/{id}",
            "skills/**"
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
            "/auth/**",
            "/error"
    });

    private final String[] role;
    private final String[] urls;

    SecurityUrls(String[] role, String[] urls) {
        this.role = role;
        this.urls = urls;
    }

}

