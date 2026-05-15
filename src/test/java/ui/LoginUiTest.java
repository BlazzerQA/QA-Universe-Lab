package ui;

import core.UiBaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.MainPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

@Feature("UI Авторизация - Регистрация пользователей")
public class LoginUiTest extends UiBaseTest {

    LoginPage loginPage = new LoginPage();
    MainPage mainPage = new MainPage();

    private String generateUniquePhone() {
        long unique = System.currentTimeMillis() % 10000000000L;
        return String.format("+7%010d", unique);
    }

    @Test
    @Description("Проверка полного успешного пути пользователя")
    public void testFullUserJourney() {
        open("/login");
        loginPage.shouldBeOnLoginPage();
        loginPage.login("+79991234567", "password123");
        mainPage.header.shouldBe(visible, Duration.ofSeconds(5));
        mainPage.header.shouldHave(text("QA Universe"));
        mainPage.logout();
        loginPage.shouldBeOnLoginPage();
    }

    @Test
    @Description("Проверка вывода ошибки при неверном пароле")
    public void testInvalidPasswordShowsError() {
        open("/login");
        loginPage.login("+79991234567", "wrong-password");
        loginPage.checkErrorMessage("Неверный логин или пароль!");
        loginPage.shouldBeOnLoginPage();
    }

    @Test
    @Description("Проверка вывода ошибки при вводе неверного формата номера")
    public void testInvalidPhoneFormat() {
        open("/login");
        loginPage.login("8999112233", "password123");
        loginPage.checkErrorMessage("Неверный формат номера. Используйте +7XXXXXXXXXX");
        loginPage.shouldBeOnLoginPage();
    }

    @Test
    @Description("Успешная регистрация нового пользователя")
    @Story("Позитивные сценарии регистрации")
    @Owner("QA Team")
    public void testSuccessfulRegistration() {
        String phone = generateUniquePhone();
        open("/login");
        loginPage.switchToRegisterTab();
        loginPage.register(phone, "Password123", "Password123");
        loginPage.shouldSeeRegistrationSuccessMessage();
    }

    @Test
    @Description("Регистрация с минимально допустимым паролем (6 символов)")
    @Story("Позитивные сценарии регистрации")
    @Owner("QA Team")
    public void testRegistrationWithMinimumPasswordLength() {
        String phone = generateUniquePhone();
        open("/login");
        loginPage.switchToRegisterTab();
        loginPage.register(phone, "123456", "123456");
        loginPage.shouldSeeRegistrationSuccessMessage();
    }

    @Test
    @Description("Регистрация с уже существующим телефоном - ожидание ошибки")
    @Story("Негативные сценарии валидации")
    @Owner("QA Team")
    public void testRegistrationWithExistingPhone() {
        String phone = generateUniquePhone();
        open("/login");
        loginPage.switchToRegisterTab();
        loginPage.register(phone, "Password123", "Password123");
        loginPage.shouldSeeRegistrationSuccessMessage();
        loginPage.switchToRegisterTab();
        loginPage.register(phone, "Password456", "Password456");
        loginPage.shouldSeeRegistrationErrorMessage("Номер уже зарегистрирован");
    }

    @Test
    @Description("Регистрация с паролем менее 6 символов - ошибка валидации")
    @Story("Негативные сценарии валидации")
    @Owner("QA Team")
    public void testRegistrationWithPasswordTooShort() {
        String phone = generateUniquePhone();
        open("/login");
        loginPage.switchToRegisterTab();
        loginPage.register(phone, "12345", "12345");
        loginPage.shouldSeePasswordError("Пароль должен содержать минимум 6 символов");
    }

    @Test
    @Description("Регистрация с несовпадающими паролями - ошибка валидации")
    @Story("Негативные сценарии валидации")
    @Owner("QA Team")
    public void testRegistrationWithPasswordsNotMatching() {
        String phone = generateUniquePhone();
        open("/login");
        loginPage.switchToRegisterTab();
        loginPage.register(phone, "Password123", "Password456");
        loginPage.shouldSeeConfirmError("Пароли не совпадают");
    }

    @Test
    @Description("Регистрация с неверным форматом телефона - ошибка валидации")
    @Story("Негативные сценарии валидации")
    @Owner("QA Team")
    public void testRegistrationWithInvalidPhoneFormat() {
        String phone = "89991112233";
        open("/login");
        loginPage.switchToRegisterTab();
        loginPage.register(phone, "Password123", "Password123");
        loginPage.shouldSeePhoneError("Неверный формат. Используйте +7XXXXXXXXXX");
    }

    @Test
    @Description("Регистрация с пустым телефоном - ошибка валидации")
    @Story("Негативные сценарии валидации")
    @Owner("QA Team")
    public void testRegistrationWithEmptyPhone() {
        open("/login");
        loginPage.switchToRegisterTab();
        loginPage.register("", "Password123", "Password123");
        loginPage.shouldSeePhoneError("Неверный формат. Используйте +7XXXXXXXXXX");
    }

    @Test
    @Description("Регистрация с телефоном из 11 цифр после +7 - ошибка валидации")
    @Story("Граничные случаи")
    @Owner("QA Team")
    public void testRegistrationWithTooManyDigitsInPhone() {
        String phone = "+799912345678";
        open("/login");
        loginPage.switchToRegisterTab();
        loginPage.register(phone, "Password123", "Password123");
        loginPage.shouldSeePhoneError("Неверный формат. Используйте +7XXXXXXXXXX");
    }

    @Test
    @Description("Регистрация с пустым паролем - ошибка валидации")
    @Story("Граничные случаи")
    @Owner("QA Team")
    public void testRegistrationWithEmptyPassword() {
        String phone = generateUniquePhone();
        open("/login");
        loginPage.switchToRegisterTab();
        loginPage.register(phone, "", "");
        loginPage.shouldSeePasswordError("Пароль должен содержать минимум 6 символов");
    }
}