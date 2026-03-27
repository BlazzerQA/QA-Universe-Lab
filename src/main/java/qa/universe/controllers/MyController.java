package qa.universe.controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

    @GetMapping("/welcome")
    public String welcomePage(Model model) {
        model.addAttribute("message","Привет, Spring работает!");
        return "index";  //TODO: Разобраться почему не возвращается message
    }
}
