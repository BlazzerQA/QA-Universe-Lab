package core;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class UiBaseTest extends BaseTest {

    @BeforeMethod
    public void uiSetup() {
        Configuration.baseUrl = "http://localhost:" + port;
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.timeout = 5000;

        Configuration.reportsFolder = "target/allure-results/screenshots";

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));
    }


    @AfterMethod
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}
