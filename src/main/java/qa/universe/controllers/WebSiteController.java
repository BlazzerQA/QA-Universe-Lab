package qa.universe.controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSiteController {

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login"; // Перенаправляем пользователя
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("message","QA Universe Auth");
        return "login";  //TODO: Разобраться почему не возвращается message
    }
}
