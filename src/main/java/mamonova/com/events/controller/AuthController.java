package mamonova.com.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mamonova.com.events.dto.request.SignInRequest;
import mamonova.com.events.dto.request.SignUpRequest;
import mamonova.com.events.dto.response.AuthResponse;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpRequest request) {
        User user = userService.register(request);
        String token = tokenService.generateToken(user.getId());

        return ResponseEntity.ok(new AuthResponse(
                token,
                user.getEmail(),
                user.getUsername()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody SignInRequest request) {
        User user = userService.login(request);
        String token = tokenService.generateToken(user.getId());

        return ResponseEntity.ok(new AuthResponse(
                token,
                user.getEmail(),
                user.getUsername()
        ));
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