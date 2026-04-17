package api;

import core.BaseTest;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

public class ProductsApiTest extends BaseTest {

    private static String createdProductId;

    private Map<String, Object> createProduct(String productName, BigDecimal price) {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("productName", productName);
        productMap.put("price", price);
        return productMap;
    }

    @AfterMethod
    public void clean() {
        if (createdProductId != null) {
            when()
                    .delete("/api/products/" + createdProductId)
                    .then()
                    .statusCode(anyOf(is(200), is(404)));
            createdProductId = null;
        }
    }

    @Test
    @Description("Getting a list of all products")
    public void testGetProduct() {
        when()
                .get("/api/products")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    @Description("Positive test: adding a product")
    public void testAddProduct() {
        Map<String, Object> product = createProduct("iPhone", new BigDecimal("999.99"));

        createdProductId =
                given()
                        .contentType(ContentType.JSON)
                        .body(product)
                        .when()
                        .post("/api/products")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("product", equalTo("iPhone"))
                        .body("price", equalTo(999.99f))
                        .body("productId", notNullValue())
                        .extract()
                        .path("productId");
    }

    @Test
    @Description("Checking that the product has appeared in the list")
    public void testProductExistsInList() {
        Map<String, Object> product = createProduct("iPhone", new BigDecimal("999.99"));

        createdProductId =
                given()
                        .contentType(ContentType.JSON)
                        .body(product)
                        .when()
                        .post("/api/products")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("productId");

        when()
                .get("/api/products")
                .then()
                .log().all()
                .statusCode(200)
                .body("productName", hasItem("iPhone"));
    }

    @Test
    @Description("DELETE /api/products/{productId} – successful deleting an existent product")
    public void testDeleteProduct() {
        Map<String, Object> product = createProduct("iPhone", new BigDecimal("999.99"));

        createdProductId =
                given()
                        .contentType(ContentType.JSON)
                        .body(product)
                        .when()
                        .post("/api/products")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("productId");

        when()
                .delete("/api/products/" + createdProductId)
                .then()
                .log().all()
                .statusCode(200)
                .body(equalTo("Product deleted successfully"));

        createdProductId = null;
    }

    @Test
    @Description("DELETE /api/products/{productId} – deleting a non-existent product")
    public void testDeleteNonExistingProduct() {
        when()
                .delete("/api/products/non-existing-id")
                .then()
                .log().all()
                .statusCode(404)
                .body("error", equalTo("Product not found"));
    }

    @Test
    @Description("Negative test: a product without a name")
    public void testAddProductWithoutName() {
        Map<String, Object> product = createProduct(null, new BigDecimal("100"));

        given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/api/products")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", containsString("Product name"));
    }

    @Test
    @Description("Negative test: adding a product without a price")
    public void testAddProductWithoutPrice() {
        Map<String, Object> product = new HashMap<>();
        product.put("productName", "Samsung");

        given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/api/products")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", containsString("Price"));
    }

    @Test
    @Description("Negative test: adding a product with a negative price")
    public void testAddProductWithNegativePrice() {
        Map<String, Object> product = createProduct("Samsung", new BigDecimal("-10"));

        given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/api/products")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", containsString("positive"));
    }

    @Test
    @Description("Boundary test: adding a product with a zero price")
    public void testAddProductWithZeroPrice() {
        Map<String, Object> product = createProduct("Free Product", BigDecimal.ZERO);

        given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/api/products")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    @Description("Boundary test: adding a product with a very high price")
    public void testAddProductWithLargePrice() {
        Map<String, Object> product =
                createProduct("Luxury Product", new BigDecimal("9999999999.99"));

        createdProductId =
                given()
                        .contentType(ContentType.JSON)
                        .body(product)
                        .when()
                        .post("/api/products")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("product", equalTo("Luxury Product"))
                        .extract()
                        .path("productId");
    }
}