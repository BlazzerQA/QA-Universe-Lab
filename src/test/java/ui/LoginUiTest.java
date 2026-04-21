package ui;

import core.UiBaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.RoadmapPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class LoginUiTest extends UiBaseTest {

    LoginPage loginPage = new LoginPage();
    RoadmapPage roadmapPage = new RoadmapPage();

    @Test
    @Description("Проверка полного успешного пути пользователя")
    public void testFullUserJourney() {
        open("/login");
        $("h1").shouldHave(text("QA Universe Auth"));
        loginPage.login("+79991234567", "password123");

        // Просто ждём немного после логина
        sleep(2000);

        roadmapPage.header.shouldHave(text("Roadmap: QA Automation"));
        roadmapPage.logout();
        $("button").shouldBe(visible);
    }

    @Test
    @Description("Проверка вывода ошибки при неверном пароле")
    public void testInvalidPasswordShowsError() {
        open("/login");
        loginPage.login("+79991234567", "wrong-password");
        loginPage.checkErrorMessage("Неверный пароль!");
        $("h1").shouldHave(text("QA Universe Auth"));
    }

    @Test
    @Description("Проверка вывода ошибки при вводе неверного формата номера")
    public void testInvalidPhoneFormat() {
        open("/login");
        loginPage.login("8999112233", "password123");
        loginPage.checkErrorMessage("Неверный формат номера. Используйте +7XXXXXXXXXX");
    }
}