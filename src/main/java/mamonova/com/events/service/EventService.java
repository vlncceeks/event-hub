package mamonova.com.events.service;

import lombok.RequiredArgsConstructor;
import mamonova.com.events.dto.request.CreateEventRequest;
import mamonova.com.events.dto.request.UpdateEventRequest;
import mamonova.com.events.exception.BadRequestException;
import mamonova.com.events.exception.NotFoundException;
import mamonova.com.events.model.Event;
import mamonova.com.events.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (request.getTotalSeats() != null) {
            int diff = request.getTotalSeats() - event.getTotalSeats();
            if (event.getAvailableSeats() + diff < 0) throw new BadRequestException("Количество мест не может быть меньше уже забронированных");
            event.setTotalSeats(request.getTotalSeats());
            event.setAvailableSeats(event.getAvailableSeats() + diff);
        }
        event.setImageUrl(request.getImageUrl() != null ? request.getImageUrl() : null);
        return eventRepository.save(event);
    }

    @Transactional
    public Event findByIdForUpdate(Long id) {
        return eventRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    @Transactional(readOnly = true)
    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    public void delete(Long id) {
        eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        eventRepository.deleteById(id);
    }
}
