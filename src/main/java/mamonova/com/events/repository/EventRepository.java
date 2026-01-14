package mamonova.com.events.repository;

import mamonova.com.events.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Override
    Optional<Event> findById(Long aLong);

}
