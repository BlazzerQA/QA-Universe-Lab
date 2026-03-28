package qa.universe.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Pattern(regexp = "^\\+7\\d{10}$", message = "Неверный формат номера. Используйте +7XXXXXXXXXX")
    private String phone;

    private String password;


}
