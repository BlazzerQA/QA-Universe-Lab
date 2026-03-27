package qa.universe.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {

    @Pattern(regexp = "^\\+7\\d{10}$", message = "Неверный формат номера. Используйте +7XXXXXXXXXX")
    private String phone;

    private String password;


}
