package mamonova.com.events.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EventShortResponse {

    private Long id;
    private String title;
    private LocalDateTime date;
    private String imageUrl;
    private Integer availableSeats;
}
