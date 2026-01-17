package mamonova.com.events.mapper;

import mamonova.com.events.dto.response.UserResponse;
import mamonova.com.events.dto.response.UserShortResponse;
import mamonova.com.events.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().toString()
        );
    }

    public List<UserResponse> toDtoList(List<User> users) {
        return users.stream()
                .map(this::toDto)
                .toList();
    }

    public UserShortResponse toShortDto(User user) {
        return new UserShortResponse(
                user.getId(),
                user.getUsername()
        );
    }
}
