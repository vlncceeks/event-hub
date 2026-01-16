package mamonova.com.events.service;

import lombok.RequiredArgsConstructor;
import mamonova.com.events.exception.BadRequestException;
import mamonova.com.events.exception.NotFoundException;
import mamonova.com.events.model.Event;
import mamonova.com.events.model.EventRegistration;
import mamonova.com.events.model.User;
import mamonova.com.events.repository.EventRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final EventRegistrationRepository registrationRepository;
    private final EventService eventService;
    private final UserService userService;

    @Transactional
    public EventRegistration register(Long userId, Long eventId) {

        Event event = eventService.findByIdForUpdate(eventId);
        User user = userService.findById(userId);

        if (event.isFullyBooked()) {
            throw new BadRequestException("No available seats");
        }

        if (registrationRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new BadRequestException("Already registered");
        }

        EventRegistration registration = new EventRegistration();
        registration.setUser(user);
        registration.setEvent(event);

        event.setAvailableSeats(event.getAvailableSeats() - 1);

        return registrationRepository.save(registration);
    }

    @Transactional
    public void cancel(Long userId, Long eventId) {
        EventRegistration registration = registrationRepository
                .findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Registration not found"));

        Event event = registration.getEvent();

        event.setAvailableSeats(event.getAvailableSeats() + 1);

        registrationRepository.delete(registration);
    }

    @Transactional(readOnly = true)
    public List<EventRegistration> getUserRegistrations(Long userId) {
        return registrationRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<EventRegistration> getEventRegistrations(Long eventId) {
        return registrationRepository.findAllByEventId(eventId);
    }
}
