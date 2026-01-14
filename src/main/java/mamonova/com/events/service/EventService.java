package mamonova.com.events.service;

import lombok.RequiredArgsConstructor;
import mamonova.com.events.dto.request.CreateEventRequest;
import mamonova.com.events.dto.request.UpdateEventRequest;
import mamonova.com.events.exception.NotFoundException;
import mamonova.com.events.model.Event;
import mamonova.com.events.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event create(CreateEventRequest request) {
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDate(request.getDate());
        event.setTotalSeats(request.getTotalSeats());
        event.setImageUrl(request.getImageUrl());
        event.setAvailableSeats(request.getTotalSeats());
        return eventRepository.save(event);
    }

    public Event update(Long id, UpdateEventRequest request) {
        Event event = findById(id);
        event.setTitle(request.getTitle() != null ? request.getTitle() : event.getTitle());
        event.setDescription(request.getDescription() != null ? request.getDescription() : event.getDescription());
        event.setDate(request.getDate() != null ? request.getDate() : event.getDate());
        event.setTotalSeats(request.getTotalSeats() != null ? request.getTotalSeats() : event.getTotalSeats());
        event.setImageUrl(request.getImageUrl() != null ? request.getImageUrl() : null);
        return eventRepository.save(event);
    }

    public Event findById(Long id) {
        if (eventRepository.findById(id) == null) {
            throw new NotFoundException("Event not found");
        }
        return eventRepository.findById(id).get();
    }

    public void delete(Long id) {
        if (eventRepository.findById(id) == null) {
            throw new NotFoundException("Event not found");
        }
        eventRepository.deleteById(id);
    }
}
