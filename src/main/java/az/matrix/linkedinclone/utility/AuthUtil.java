package az.matrix.linkedinclone.utility;

import az.matrix.linkedinclone.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final JwtUtil jwtUtil;

    public Long resolveAndAuthorizeUser(HttpServletRequest request, Long targetId) {
        Long authenticatedId = getUserId(jwtUtil.resolveClaims(request));
        authorizeUser(authenticatedId, targetId);
        return authenticatedId;
    }

    private Long getUserId(Claims claims) {
        if (claims == null || !claims.containsKey("user_id")) {
            throw new UnauthorizedException("INVALID_TOKEN");
        }
        return Long.valueOf(claims.get("user_id").toString());
    }

    private void authorizeUser(Long authenticatedId, Long targetId) {
        if (authenticatedId == null || !authenticatedId.equals(targetId)) {
            throw new UnauthorizedException("UNAUTHORIZED_ACCESS");
        }
    }
}
