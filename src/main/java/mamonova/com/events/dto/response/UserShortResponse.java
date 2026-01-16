package mamonova.com.events.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserShortResponse {

    private Long id;
    private String username; // или email
}
