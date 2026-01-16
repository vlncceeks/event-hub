package mamonova.com.events.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EventResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime date;

    private Integer totalSeats;
    private Integer availableSeats;

    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
