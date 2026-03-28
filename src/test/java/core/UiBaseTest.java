package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class UiBaseTest extends BaseTest {

    @BeforeMethod
    public void uiSetup() {
        Configuration.baseUrl = "http://localhost:" + port;
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 5000;
    }


    @AfterMethod
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}
