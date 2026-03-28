import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import qa.universe.MainApplication;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MainApplication.class
)
public class BaseTest extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    protected int port;

    @BeforeMethod
    public void setUp() throws Exception {

        this.springTestContextPrepareTestInstance();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;

        System.out.println("--- ТЕСТ ЗАПУЩЕН НА ПОРТУ: " + port + " ---");
    }
}
