package qa.universe.controllers.ui;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import qa.universe.dto.LoginRequest;
import qa.universe.models.User;
import qa.universe.repositories.UserRepository;

import java.util.Optional;

@Controller
public class WebSiteController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login"; // Перенаправляем пользователя
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String performLogin(@Valid @ModelAttribute("loginRequest") LoginRequest request,
                               BindingResult bindingResult, Model model, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Ищем юзера в БД по телефону
        Optional<User> userInDb = userRepository.findUserByPhone(request.getPhone());

        if (userInDb.isPresent() && passwordEncoder.matches(request.getPassword(),userInDb.get().getPassword())) {
            session.setAttribute("userId", userInDb.get().getId());
            session.setAttribute("userPhone", userInDb.get().getPhone());
            session.setAttribute("userName", userInDb.get().getFullName());
            return "redirect:/main";
        } else {
            model.addAttribute("error","Неверный логин или пароль!");
            return "login";
        }
    }

    @GetMapping("/roadmap")
    public String showRoadmap(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("userName");
        if (userName == null || userName.isEmpty()) {
            userName = (String) session.getAttribute("userPhone");
        }
        model.addAttribute("userName", userName);
        return "roadmap";
    }

    @GetMapping("/main")
    public String showMainPage(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("userName");
        if (userName == null || userName.isEmpty()) {
            userName = (String) session.getAttribute("userPhone");
        }
        model.addAttribute("userName", userName);
        return "main";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/collections")
    public String collectionsPage() {
        return "collections";
    }

    @GetMapping("/mocks")
    public String mocksPage() {
        return "mocks";
    }

    @GetMapping("/json-formatter")
    public String jsonFormatterPage(Model model) {
        String exampleJson = "{\n  \"name\": \"QA Universe\"," +
                "\n  \"tools\": [\"Spring\", \"Thymeleaf\", \"JUnit\"]," +
                "\n  \"config\": {\"timeout\": 30}\n}";
        model.addAttribute("jsonData", exampleJson);
        return "json-formatter";
    }


}
