package api;

import core.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import qa.universe.dto.RegistrationRequest;
import qa.universe.models.User;
import qa.universe.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Feature("API Авторизация - Регистрация пользователей")
public class RegistrationApiTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    private final List<String> createdPhones = new ArrayList<>();

    @AfterMethod
    public void cleanup() {
        for (String phone : createdPhones) {
            Optional<User> userOpt = userRepository.findUserByPhone(phone);
            userOpt.ifPresent(user -> userRepository.delete(user));
        }
        createdPhones.clear();
    }

    private String generateUniquePhone() {
        long unique = System.currentTimeMillis() % 10000000000L;
        return String.format("+7%010d", unique);
    }

    @Test
    @Description("Успешная регистрация нового пользователя - проверка статуса 201 и структуры ответа")
    @Story("Позитивные сценарии регистрации")
    @Owner("QA Team")
    public void testSuccessfulRegistration() {
        String phone = generateUniquePhone();
        RegistrationRequest request = new RegistrationRequest();
        request.setPhone(phone);
        request.setPassword("Password123");
        request.setFullName("Иван Иванов");

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/api/auth/register")
        .then()
                .log().all()
                .statusCode(201)
                .body("message", equalTo("Пользователь успешно зарегистрирован"))
                .body("phone", equalTo(phone))
                .body("token", notNullValue());

        createdPhones.add(phone);
    }

    @Test
    @Description("Регистрация с минимально допустимым паролем (6 символов)")
    @Story("Позитивные сценарии регистрации")
    @Owner("QA Team")
    public void testRegistrationWithMinimumPasswordLength() {
        String phone = generateUniquePhone();
        RegistrationRequest request = new RegistrationRequest();
        request.setPhone(phone);
        request.setPassword("123456");

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/api/auth/register")
        .then()
                .log().all()
                .statusCode(201)
                .body("message", equalTo("Пользователь успешно зарегистрирован"))
                .body("phone", equalTo(phone))
                .body("token", notNullValue());

        createdPhones.add(phone);
    }

    @Test
    @Description("Регистрация с максимально допустимым паролем (72 символа)")
    @Story("Позитивные сценарии регистрации")
    @Owner("QA Team")
    public void testRegistrationWithMaximumPasswordLength() {
        String phone = generateUniquePhone();
        RegistrationRequest request = new RegistrationRequest();
        request.setPhone(phone);
        request.setPassword("a".repeat(72));

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/api/auth/register")
        .then()
                .log().all()
                .statusCode(201)
                .body("message", equalTo("Пользователь успешно зарегистрирован"))
                .body("phone", equalTo(phone))
                .body("token", notNullValue());

        createdPhones.add(phone);
    }

    @Test
    @Description("Регистрация с уже существующим телефоном - ожидание конфликта 409")
    @Story("Негативные сценарии валидации")
    @Owner("QA Team")
    public void testRegistrationWithExistingPhone() {
        String phone = generateUniquePhone();
        RegistrationRequest firstRequest = new RegistrationRequest();
        firstRequest.setPhone(phone);
        firstRequest.setPassword("Password123");

        given()
                .contentType(ContentType.JSON)
                .body(firstRequest)
        .when()
                .post("/api/auth/register")
        .then()
                .statusCode(201);

        createdPhones.add(phone);

        RegistrationRequest secondRequest = new RegistrationRequest();
        secondRequest.setPhone(phone);
        secondRequest.setPassword("DifferentPassword456");

        given()
                .contentType(ContentType.JSON)
                .body(secondRequest)
        .when()
                .post("/api/auth/register")
        .then()
                .log().all()
                .statusCode(409)
                .body("error", equalTo("Номер уже зарегистрирован"));
    }

    @Test
    @Description("Регистрация с паролем менее 6 символов - ожидание ошибки 400")
    @Story("Негативные сценарии валидации")
    @Owner("QA Team")
    public void testRegistrationWithPasswordTooShort() {
        String phone = generateUniquePhone();
        RegistrationRequest request = new RegistrationRequest();
        request.setPhone(phone);
        request.setPassword("12345");

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/api/auth/register")
        .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    @Description("Регистрация с паролем более 72 символов - ожидание ошибки 400")
    @Story("Негативные сценарии валидации")
    @Owner("QA Team")
    public void testRegistrationWithPasswordTooLong() {
        String phone = generateUniquePhone();
        RegistrationRequest request = new RegistrationRequest();
        request.setPhone(phone);
        request.setPassword("a".repeat(73));

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/api/auth/register")
        .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    @Description("Регистрация с пустым телефоном - ожидание ошибки 400")
    @Story("Негативные сценарии валидации")
    @Owner("QA Team")
    public void testRegistrationWithEmptyPhone() {
        RegistrationRequest request = new RegistrationRequest();
        request.setPhone("");
        request.setPassword("Password123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/api/auth/register")
        .then()
                .log().all()
                .statusCode(400);
    }



    @Test
    @Description("Регистрация без поля phone в JSON - ожидание ошибки 400")
    @Story("Негативные сценарии валидации")
    @Owner("QA Team")
    public void testRegistrationWithoutPhoneField() {
        String json = "{\"password\":\"Password123\"}";

        given()
                .contentType(ContentType.JSON)
                .body(json)
        .when()
                .post("/api/auth/register")
        .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    @Description("Регистрация без поля password в JSON - ожидание ошибки 400")
    @Story("Негативные сценарии валидации")
    @Owner("QA Team")
    public void testRegistrationWithoutPasswordField() {
        String json = "{\"phone\":\"+79991234567\"}";

        given()
                .contentType(ContentType.JSON)
                .body(json)
        .when()
                .post("/api/auth/register")
        .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    @Description("Регистрация с телефоном из 11 цифр после +7 - ожидание ошибки 400")
    @Story("Граничные случаи")
    @Owner("QA Team")
    public void testRegistrationWithTooManyDigitsInPhone() {
        String phone = "+799912345678";
        RegistrationRequest request = new RegistrationRequest();
        request.setPhone(phone);
        request.setPassword("Password123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/api/auth/register")
        .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    @Description("Регистрация с паролем из пробелов - ожидание ошибки 400")
    @Story("Граничные случаи")
    @Owner("QA Team")
    public void testRegistrationWithPasswordOnlySpaces() {
        String phone = generateUniquePhone();
        RegistrationRequest request = new RegistrationRequest();
        request.setPhone(phone);
        request.setPassword("      ");

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/api/auth/register")
        .then()
                .log().all()
                .statusCode(400);

        createdPhones.add(phone);
    }
}
