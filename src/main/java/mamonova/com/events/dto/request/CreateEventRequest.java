package mamonova.com.events.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@Schema(description = "Запрос на создание мероприятия")
public class CreateEventRequest {

    @Schema(
            description = "Название мероприятия",
            example = "Конференция по Spring Boot 2024",
            minLength = 5,
            maxLength = 70
    )
    @NotBlank(message = "Название мероприятия не может быть пустым")
    @Size(min = 5, max = 70, message = "Название мероприятия должно содержать от 5 до 70 символов")
    @Pattern(
            regexp = "^[\\p{L}\\d\\s\\p{P}]+$",
            message = "Название содержит недопустимые символы"
    )
    private String title;

    @Schema(
            description = "Описание мероприятия",
            example = "Ежегодная конференция для Java-разработчиков",
            minLength = 5,
            maxLength = 250
    )
    @NotBlank(message = "Описание мероприятия не может быть пустым")
    @Size(min = 5, max = 250, message = "Описание мероприятия должно содержать от 5 до 250 символов")
    private String description;

    @Schema(
            description = "Дата и время проведения",
            example = "2024-12-25T18:00:00",
            format = "date-time"
    )
    @NotNull(message = "Дата проведения не может быть пустой")
    @FutureOrPresent(message = "Дата проведения должна быть сегодня или в будущем")
    private LocalDateTime date;

    @Schema(
            description = "Количество мест",
            example = "100",
            minimum = "1",
            maximum = "10000"
    )
    @NotNull(message = "Количество мест не может быть пустым")
    @Min(value = 1, message = "Количество мест не может быть меньше 1")
    @Max(value = 10000, message = "Количество мест не может превышать 10000")
    private Integer totalSeats;

    @Schema(
            description = "URL изображения мероприятия",
            example = "https://example.com/images/conference.jpg",
            maxLength = 2048
    )
    @Size(max = 2048, message = "Длина URL изображения не должна превышать 2048 символов")
    @Pattern(
            regexp = "^$|^https://.*\\.(jpg|jpeg|png|gif|webp)$",
            message = "Изображение должно быть HTTPS-ссылкой на jpg/jpeg/png/gif/webp"
    )
    private String imageUrl;
}
