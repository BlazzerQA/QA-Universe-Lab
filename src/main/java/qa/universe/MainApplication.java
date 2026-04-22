package qa.universe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import qa.universe.models.User;
import qa.universe.repositories.UserRepository;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findUserByPhone("+79991234567").isEmpty()) {
                String encodedPassword = passwordEncoder.encode("password123");
                userRepository.save(new User(null, "+79991234567", encodedPassword));
                System.out.println("Тестовый пользователь создан!");
            }
        };
    }
}