package mamonova.com.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mamonova.com.events.dto.request.SignInRequest;
import mamonova.com.events.dto.request.SignUpRequest;
import mamonova.com.events.dto.response.AuthResponse;
import mamonova.com.events.mapper.AuthMapper;
import mamonova.com.events.model.User;
import mamonova.com.events.service.TokenService;
import mamonova.com.events.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthMapper authMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody SignUpRequest request) {
        User user = userService.register(request);
        String token = tokenService.generateToken(user.getId());

        return ResponseEntity.ok(
                authMapper.toDto(token, user)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody SignInRequest request) {
        User user = userService.login(request);
        String token = tokenService.generateToken(user.getId());

        return ResponseEntity.ok(
                authMapper.toDto(token, user)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Authorization header is required");
        }

        String token = authHeader.substring(7);
        tokenService.invalidateToken(token);

        return ResponseEntity.ok("Logged out successfully");
    }
}