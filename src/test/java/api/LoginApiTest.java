package api;

import core.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import qa.universe.dto.LoginRequest;
import qa.universe.dto.RegistrationRequest;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class LoginApiTest extends BaseTest {

    // Генерируем уникальные данные для каждого теста
    private String testPhone;
    private String testPassword;

    @BeforeMethod
    public void createTestUser() {
        // Генерируем случайный номер телефона, чтобы не было конфликтов
        testPhone = "+79" + String.format("%09d", (long)(Math.random() * 1_000_000_000L));
        testPassword = "pass123";

        System.out.println("--- Создаем тестового пользователя: " + testPhone + " ---");

        // Регистрируем пользователя через API
        RegistrationRequest regRequest = new RegistrationRequest();
        regRequest.setPhone(testPhone);
        regRequest.setPassword(testPassword);

        given()
                .contentType(ContentType.JSON)
                .body(regRequest)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(201);  // CREATED — пользователь создан
    }

    @Test
    public void testSuccessfulLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPhone(testPhone);
        loginRequest.setPassword(testPassword);

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/auth/login")
                .then()
                .log().all()
                .statusCode(200)
                .body("token", notNullValue());  // ← Убрали проверку "status", оставили только "token"
    }

    @Test
    public void testFailedLoginWithWrongPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPhone(testPhone);
        loginRequest.setPassword("WRONG_PASSWORD");

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/auth/login")
                .then()
                .log().all()
                .statusCode(401)  // UNAUTHORIZED
                .body("error", org.hamcrest.Matchers.equalTo("Неверный логин или пароль"));
    }
}