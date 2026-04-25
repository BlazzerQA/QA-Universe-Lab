package qa.universe.controllers.api;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import qa.universe.dto.LoginRequest;
import qa.universe.dto.ProfileRequest;
import qa.universe.dto.RegistrationRequest;
import qa.universe.models.User;
import qa.universe.repositories.UserRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {

        Optional<User> existingUser = userRepository.findUserByPhone(request.getPhone());
        if (existingUser.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Номер уже зарегистрирован"));
        }

        User newUser = new User();
        newUser.setPhone(request.getPhone());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(newUser);
        String randomToken = UUID.randomUUID().toString();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message","Пользователь успешно зарегистрирован",
                        "phone", request.getPhone(),
                        "token", randomToken));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        var userOpt = userRepository.findUserByPhone(request.getPhone());
        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неверный логин или пароль"));
        }

        var user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неверный логин или пароль"));
        }

        String randomToken = UUID.randomUUID().toString();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("token", randomToken));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Не авторизован"));
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Пользователь не найден"));
        }

        ProfileRequest dto = new ProfileRequest();
        dto.setPhone(user.getPhone());
        dto.setFullName(user.getFullName());

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileRequest profileDto, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Не авторизован"));
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Пользователь не найден"));
        }

        user.setFullName(profileDto.getFullName());
        userRepository.save(user);

        session.setAttribute("userName", user.getFullName());

        return  ResponseEntity.ok(Map.of("message","Данные обновлены", "fullName", user.getFullName()));
    }
}
