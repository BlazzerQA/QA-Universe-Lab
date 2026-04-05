package qa.universe.controllers.api;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import qa.universe.model.Product;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductsRestController {

    private final Map<String, Product> products = new LinkedHashMap<>();

    @GetMapping
    public Collection<Product> getProducts() {
        return products.values();
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()));
        }

        if (product.getProductId() == null || product.getProductId().isBlank()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        products.put(product.getProductId(), product);
        return ResponseEntity.ok(Map.of(
                "Product",product.getProductName(),
                "Price",product.getPrice(),
                "Product ID",product.getProductId()));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        Product removedProduct = products.remove(productId);

        if(removedProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found"));
        }
        return ResponseEntity.ok("Product deleted successfully");
    }

}
