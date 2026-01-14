package mamonova.com.events.service;

import lombok.RequiredArgsConstructor;
import mamonova.com.events.exception.UnauthorizedException;
import mamonova.com.events.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;

    public Long getCurrentUser(String authHeader) {
        String token = getToken(authHeader);
        Long userId = tokenService.getUserIdFromToken(token);

        if (userId == null) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        return userId;
    }

    public void deleteCurrentUser(String authHeader) {
        String token = getToken(authHeader);
        tokenService.invalidateToken(token);
    }

    private String getToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization header is required");
        }
        String token = authHeader.substring(7);
        return token;
    }

}
