package api;

import core.BaseTest;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

public class CollectionsApiTest extends BaseTest {

    @Test
    @Description("Проверка получения списка логов")
    public void testGetLogs() {
        when()
                .get("/api/collections/list")
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", is(greaterThan(0)))
                .body("[0]",equalTo("Started"));
    }


    @Test
    @Description("Добавление нового лога в List")
    public void testAddLog() {
        String newLog = "User_Logged_In";

        given()
                .queryParam("log",newLog)
        .when()
                .post("/api/collections/list")
        .then()
                .log().body()
                .statusCode(200);

        when()
                .get("/api/collections/list")
        .then()
                .body("", hasItem(newLog));
    }

    @Test
    @Description("Проверка уникальности в Set")
    public void testSetUniqueness() {
        int existingId = 101;

        given()
                .queryParam("id", existingId)
        .when()
                .post("/api/collections/set")
        .then()
                .log().all()
                .statusCode(200)
                .body(equalTo("User already exists!"));
    }

    @Test
    @Description("Проверка структуры конфигурации (Map)")
    public void testMapConfig() {
        when()
                .get("/api/collections/map")
        .then()
                .log().all()
                .statusCode(200)
                .body("env", is("QA"))
                .body("timeout", is("5000"));
    }

}
