package qa.universe.controllers.api;


import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/products")
public class ProductsRestController {

    private final List<String> products = new ArrayList<>();

    @GetMapping
    public List<String> getProducts(){
        return products;
    }

    @PostMapping
    public String addProduct(@RequestParam String name){
        products.add(name);
        return "Product added";
    }

    @DeleteMapping
    public String deleteProduct(@RequestParam String name){
        boolean removed = products.remove(name);
        if (removed){
            return "Product removed";}
        else {
            return "Product not found";}
    }

}
