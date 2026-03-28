package ui;

import core.UiBaseTest;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.RoadmapPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginUiTest extends UiBaseTest {

    LoginPage loginPage = new LoginPage();
    RoadmapPage roadmapPage = new RoadmapPage();

    @Test
    @Description("Проверка полного успешного пути пользователя")
    public void testFullUserJourney() {

        //1. Открываем страничку
        open("/login");

        //2. Проверяем, что это она) по заголовку
        $("h1").shouldHave(text("QA Universe Auth"));

        //3. Вводим успешные данные
        loginPage.login("+79991112233", "qwerty");

        //5. Проверяем, что находимся на нужной странице
        roadmapPage.header.shouldHave(text("Roadmap: QA Automation"));

        //6. Выходим по кнопке "Выйти"
        roadmapPage.logout();

        //7. Проверяем, что вышли - просто если видим кнопку LOGIN
        $("button").shouldBe(visible);
    }


    @Test
    @Description("Проверка вывода ошибки при неверном пароле")
    public void testInvalidPasswordShowsError() {
        open("/login");

        loginPage.login("+79991112233", "some-password");

        loginPage.checkErrorMessage("Неверный пароль!");

        $("h1").shouldHave(text("QA Universe Auth"));
    }


    @Test
    @Description("Проверка вывода ошибки при вводе неверного формата номера")
    public void testInvalidPhoneFormat() {
        open("/login");

        loginPage.login("89991112233", "qwerty");

        loginPage.checkErrorMessage("Неверный формат номера. Используйте +7XXXXXXXXXX");
    }
}
