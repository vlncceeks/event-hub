package mamonova.com.events.mapper;

import mamonova.com.events.dto.response.EventShortResponse;
import mamonova.com.events.dto.response.RegistrationResponse;
import mamonova.com.events.dto.response.UserShortResponse;
import mamonova.com.events.model.EventRegistration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistrationMapper {

    public RegistrationResponse toDto(EventRegistration registration) {
        return new RegistrationResponse(
                registration.getId(),
                registration.getRegisteredAt(),
                new EventShortResponse(
                        registration.getEvent().getId(),
                        registration.getEvent().getTitle(),
                        registration.getEvent().getDate(),
                        registration.getEvent().getImageUrl(),
                        registration.getEvent().getAvailableSeats()
                ),
                new UserShortResponse(
                        registration.getUser().getId(),
                        registration.getUser().getUsername()
                )
        );
    }

    public List<RegistrationResponse> toDtoList(List<EventRegistration> registrations) {
        return registrations.stream()
                .map(this::toDto)
                .toList();
    }
}
