package mamonova.com.events.controller;

import lombok.RequiredArgsConstructor;
import mamonova.com.events.dto.response.RegistrationResponse;
import mamonova.com.events.mapper.RegistrationMapper;
import mamonova.com.events.service.AuthService;
import mamonova.com.events.service.RegistrationService;
import mamonova.com.events.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class RegistrationController {

    private final AuthService authService;
    private final UserService userService;
    private final RegistrationService registrationService;
    private final RegistrationMapper registrationMapper;

    @PostMapping("/events/{eventId}/register")
    public ResponseEntity<RegistrationResponse> register(@PathVariable Long eventId,
                                                         @RequestHeader("Authorization") String authHeader) {

        Long userId = authService.getCurrentUser(authHeader);

        return ResponseEntity.ok(
                registrationMapper.toDto(registrationService.register(userId, eventId))
        );
    }

    @DeleteMapping("/events/{eventId}/register")
    public ResponseEntity<?> cancel(@PathVariable Long eventId,
                                    @RequestHeader("Authorization") String authHeader) {

        Long userId = authService.getCurrentUser(authHeader);

        registrationService.cancel(userId, eventId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/me/registrations")
    public ResponseEntity<List<RegistrationResponse>> myRegistrations(
            @RequestHeader("Authorization") String authHeader) {

        Long userId = authService.getCurrentUser(authHeader);

        return ResponseEntity.ok(
                registrationMapper.toDtoList(registrationService.getUserRegistrations(userId))
        );
    }

    @GetMapping("/events/{eventId}/registrations")
    public ResponseEntity<List<RegistrationResponse>> eventRegistrations(@PathVariable Long eventId,
                                                @RequestHeader("Authorization") String authHeader) {

        Long userId = authService.getCurrentUser(authHeader);
        userService.onlyAdmin(userId);

        return ResponseEntity.ok(
                registrationMapper.toDtoList(registrationService.getEventRegistrations(eventId))
        );
    }
}

