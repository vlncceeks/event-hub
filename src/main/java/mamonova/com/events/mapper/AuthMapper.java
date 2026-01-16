package mamonova.com.events.mapper;

import mamonova.com.events.dto.response.AuthResponse;
import mamonova.com.events.model.Event;
import mamonova.com.events.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    public AuthResponse toDto(String token, User user) {
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getUsername()
        );
    }
}
