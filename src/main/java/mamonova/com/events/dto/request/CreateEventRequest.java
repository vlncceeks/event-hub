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
public class CreateEventRequest {

    @Schema(
            description = "Название мероприятия",
            example = "Конференция по Spring Boot 2024",
            minLength = 5,
            maxLength = 70
    )
    @Size(min = 5, max = 70, message = "Название мероприятия должно содержать от 5 до 70 символов")
    @NotBlank(message = "Название мероприятия не может быть пустым")
    @Pattern(
            regexp = "^[\\p{L}\\d\\s\\p{P}]+$",
            message = "Название содержит недопустимые символы. Допускаются буквы, цифры, пробелы и знаки препинания"
    )
    private String title;

    @Schema(
            description = "Описание мероприятия",
            example = "Ежегодная конференция для Java-разработчиков",
            minLength = 5,
            maxLength = 250
    )
    @Size(min = 5, max = 250, message = "Описание мероприятия должно содержать от 5 до 250 символов")
    @NotBlank(message = "Описание мероприятия не может быть пустым")
    private String description;

    @Schema(
            description = "Дата и время проведения",
            example = "2024-12-25T18:00:00",
            type = "string",
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
    @Positive(message = "Количество мест должно быть положительным числом")
    private Integer totalSeats;

    @Schema(
            description = "URL изображения мероприятия",
            example = "https://example.com/images/conference.jpg",
            maxLength = 2048
    )
    @URL(
            protocol = "https",
            regexp = ".*\\.(jpg|jpeg|png|gif|webp)$",
            message = "Изображение должно быть доступно по HTTPS ссылке на файл с расширением jpg, jpeg, png, gif или webp"
    )
    @Size(max = 2048, message = "Длина URL изображения не должна превышать 2048 символов")
    private String imageUrl;
}