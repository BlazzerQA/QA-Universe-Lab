package ui;

import core.UiBaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.MainPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class LoginUiTest extends UiBaseTest {

    LoginPage loginPage = new LoginPage();
    MainPage mainPage = new MainPage();

    @Test
    @Description("Проверка полного успешного пути пользователя")
    public void testFullUserJourney() {
        open("/login");
        $("h1").shouldHave(text("QA Universe Auth"));
        loginPage.login("+79991234567", "password123");
        mainPage.header.shouldHave(text("QA Universe"));
        mainPage.logout();
        $("button").shouldBe(visible);
    }

    @Test
    @Description("Проверка вывода ошибки при неверном пароле")
    public void testInvalidPasswordShowsError() {
        open("/login");
        loginPage.login("+79991234567", "wrong-password");
        loginPage.checkErrorMessage("Неверный логин или пароль!");
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