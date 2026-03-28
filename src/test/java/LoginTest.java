
import qa.universe.dto.LoginRequest;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class LoginTest extends BaseTest {

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
                .log().all()
               .statusCode(200)
               .body("status", equalTo("success"))
               .body("token", notNullValue());

    }
}
