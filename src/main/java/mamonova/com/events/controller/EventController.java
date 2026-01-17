package mamonova.com.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mamonova.com.events.dto.request.CreateEventRequest;
import mamonova.com.events.dto.request.UpdateEventRequest;
import mamonova.com.events.dto.response.EventResponse;
import mamonova.com.events.exception.AccessDeniedException;
import mamonova.com.events.mapper.EventMapper;
import mamonova.com.events.model.Event;
import mamonova.com.events.model.Role;
import mamonova.com.events.model.User;
import mamonova.com.events.service.AuthService;
import mamonova.com.events.service.EventService;
import mamonova.com.events.service.TokenService;
import mamonova.com.events.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final AuthService authService;
    private final UserService userService;
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAll() {
        return ResponseEntity.ok(
                eventMapper.toDtoList(eventService.getAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> get(@PathVariable("id") Long eventId) {
        return ResponseEntity.ok(
                eventMapper.toDto(eventService.findById(eventId))
        );
    }

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody CreateEventRequest event,
                                    @RequestHeader(value = "Authorization") String authHeader) {
        Long userId = authService.getCurrentUser(authHeader);
        userService.onlyAdmin(userId);

        return ResponseEntity.ok(
                eventMapper.toDto(eventService.create(event))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(@PathVariable("id") Long eventId,
                                    @Valid @RequestBody UpdateEventRequest event,
                                    @RequestHeader(value = "Authorization") String authHeader) {
        Long userId = authService.getCurrentUser(authHeader);
        userService.onlyAdmin(userId);

        return ResponseEntity.ok(
                eventMapper.toDto(eventService.update(eventId, event))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long eventId,
                                    @RequestHeader(value = "Authorization") String authHeader) {
        Long userId = authService.getCurrentUser(authHeader);
        userService.onlyAdmin(userId);

        eventService.delete(eventId);
        return ResponseEntity.ok().build();
    }
}
