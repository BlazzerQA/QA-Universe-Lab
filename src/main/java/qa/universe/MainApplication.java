package qa.universe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import qa.universe.models.User;
import qa.universe.repositories.UserRepository;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.findUserByPhone("+79991234567").isEmpty()) {
                userRepository.save(new User(null, "+79991234567", "password123"));
                System.out.println("Тестовый пользователь создан!");
            }
        };
    }
}