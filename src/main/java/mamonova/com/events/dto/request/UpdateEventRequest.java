package mamonova.com.events.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
@Schema(description = "Запрос на создание мероприятия")
@Getter
@Setter
public class UpdateEventRequest {
    @Schema(description = "Название мероприятия")
    @Size(min = 5, max = 70, message = "Название мероприятия должно содержать от 5 до 70 символов")
    @NotBlank(message = "Название мероприятия не может быть пустым")
    @Pattern(regexp = "^[\\p{L}\\d\\s\\p{P}]+$", message = "Название содержит недопустимые символы")
    private String title;

    @Schema(description = "Описание мероприятия")
    @Size(min = 5, max = 250, message = "Описание мероприятия должно содержать от 5 до 250 символов")
    @NotBlank(message = "Описание мероприятия не может быть пустым")
    private String description;

    @Schema(description = "Дата проведения")
    @NotNull(message = "Дата проведения не может быть пустой")
    @NotBlank(message = "Дата проведения не может быть пустой")
    @FutureOrPresent(message = "Дата проведения должна быть сегодня или в будущем")
    private LocalDateTime date;

    @Schema(description = "Количество мест")
    @Size(min = 1, message = "Количество мест не может быть меньше 1")
    @NotBlank(message = "Количество мест не может быть пустым")
    @Positive(message = "Количество мест должно быть положительным числом")
    private Integer totalSeats;

    @Schema(description = "Изображение")
    @URL(protocol = "https",
            host = ".*\\.(jpg|jpeg|png|gif|webp)$",
            message = "Изображение должно быть доступно по HTTPS ссылке с изображением")
    @Size(max = 2048, message = "Длина URL изображения не должна превышать 2048 символов")
    private String imageUrl;
}
