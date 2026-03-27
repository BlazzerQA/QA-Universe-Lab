import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import qa.universe.dto.LoginRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTest {

    @LocalServerPort
    private int port;

    @BeforeMethod
    public void setUp(){

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void testSuccessfulLogin() {
        LoginRequest request = new LoginRequest();
        request.setPhone("+79991112233");
        request.setPassword("pass123");

        given()
               .contentType(ContentType.JSON)
               .body(request)
        .when()
               .post("/api/login")
        .then()
               .statusCode(200)
               .body("status", equalTo("success"))
               .body("token", notNullValue());


    }
}
