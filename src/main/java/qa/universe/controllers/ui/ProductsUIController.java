package qa.universe.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductsUIController {
    
    @GetMapping("/products-ui")
    public String productsUI() {
        return "products-ui";
    }
}