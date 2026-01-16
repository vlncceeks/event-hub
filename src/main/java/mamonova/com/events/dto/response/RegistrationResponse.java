package mamonova.com.events.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RegistrationResponse {

    private Long id;
    private LocalDateTime registeredAt;
    private EventShortResponse event;
    private UserShortResponse user;
}
