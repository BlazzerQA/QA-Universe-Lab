package qa.universe.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+7\\d{10}$", message = "Формат: +7XXXXXXXXXX")
    private String phone;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, max = 72, message = "Пароль должен содержать от 6 до 72 символов")
    private String password;

}
