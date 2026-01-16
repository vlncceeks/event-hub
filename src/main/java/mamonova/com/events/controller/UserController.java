package mamonova.com.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mamonova.com.events.dto.request.UpdateUserRequest;
import mamonova.com.events.dto.response.UserResponse;
import mamonova.com.events.mapper.UserMapper;
import mamonova.com.events.model.User;
import mamonova.com.events.service.AuthService;
import mamonova.com.events.service.TokenService;
import mamonova.com.events.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(
                userMapper.toDtoList(userService.getAll())
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> get(@RequestHeader(value = "Authorization") String authHeader) {
        Long userId = authService.getCurrentUser(authHeader);

        User user = userService.findById(userId);
        return ResponseEntity.ok(
                userMapper.toDto(user)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> update(
            @Valid @RequestBody UpdateUserRequest request,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        Long userId = authService.getCurrentUser(authHeader);

        User updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(
                userMapper.toDto(updatedUser)
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> delete(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = authService.getCurrentUser(authHeader);

        userService.deleteUser(userId);
        authService.deleteCurrentUser(authHeader);
        return ResponseEntity.ok().build();
    }
}