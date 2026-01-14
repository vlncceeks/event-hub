package mamonova.com.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mamonova.com.events.dto.request.CreateEventRequest;
import mamonova.com.events.dto.request.UpdateEventRequest;
import mamonova.com.events.exception.AccessDeniedException;
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

    @GetMapping
    public List<Event> getAll() {
        return eventService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.findById(eventId));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody CreateEventRequest event,
                                    @RequestHeader(value = "Authorization") String authHeader) {
        Long userId = authService.getCurrentUser(authHeader);
        userService.onlyAdmin(userId);

        Event createdEvent = eventService.create(event);
        return ResponseEntity.ok(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventRequest event,
                                    @RequestHeader(value = "Authorization") String authHeader) {
        Long userId = authService.getCurrentUser(authHeader);
        userService.onlyAdmin(userId);

        Event createdEvent = eventService.update(eventId, event);
        return ResponseEntity.ok(createdEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long eventId,
                                    @RequestHeader(value = "Authorization") String authHeader) {
        Long userId = authService.getCurrentUser(authHeader);
        userService.onlyAdmin(userId);

        eventService.delete(eventId);
        return ResponseEntity.ok().build();
    }
}
