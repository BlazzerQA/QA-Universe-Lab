package qa.universe.controllers;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import qa.universe.dto.LoginRequest;

@Controller
public class WebSiteController {

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login"; // Перенаправляем пользователя
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";  //TODO: Разобраться почему не возвращается message
    }

    @PostMapping("/login")
    public String performLogin(@Valid @ModelAttribute("loginRequest") LoginRequest request,
                               BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        if ("qwerty".equals(request.getPassword())) {
            return "redirect:/roadmap";
        } else {
            model.addAttribute("error","Неверный пароль!");
            return "login";
        }
    }

    @GetMapping("/roadmap")
    public String showRoadmap() {
        return "roadmap";
    }

    @GetMapping("/logout")
    public String logout() {
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


}
