package mamonova.com.events.repository;

import mamonova.com.events.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository
        extends JpaRepository<EventRegistration, Long> {

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    Optional<EventRegistration> findByUserIdAndEventId(Long userId, Long eventId);

    List<EventRegistration> findAllByEventId(Long eventId);

    List<EventRegistration> findAllByUserId(Long userId);
}
