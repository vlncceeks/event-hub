package mamonova.com.events.mapper;

import mamonova.com.events.dto.response.EventResponse;
import mamonova.com.events.dto.response.EventShortResponse;
import mamonova.com.events.model.Event;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventMapper {

    public EventResponse toDto(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getTotalSeats(),
                event.getAvailableSeats(),
                event.getImageUrl(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }

    public List<EventResponse> toDtoList(List<Event> events) {
        return events.stream()
                .map(this::toDto)
                .toList();
    }

    public EventShortResponse toShortDto(Event event) {
        return new EventShortResponse(
                event.getId(),
                event.getTitle(),
                event.getDate(),
                event.getImageUrl(),
                event.getAvailableSeats()
        );
    }
}
