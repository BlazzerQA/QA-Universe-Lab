package qa.universe.controllers.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qa.universe.dto.LoginRequest;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LoginRestController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        String randomToken = UUID.randomUUID().toString();

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "token", randomToken
        ));
    }

}
