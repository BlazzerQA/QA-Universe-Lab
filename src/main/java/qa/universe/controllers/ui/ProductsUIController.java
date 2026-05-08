package qa.universe.controllers.ui;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductsUIController {

    @GetMapping("/products-ui")
    public String productsUI(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        String userName = (String) session.getAttribute("userName");
        if (userName == null || userName.isEmpty()) {
            userName = (String) session.getAttribute("userPhone");
        }
        model.addAttribute("userName", userName);
        return "products-ui";
    }
}
