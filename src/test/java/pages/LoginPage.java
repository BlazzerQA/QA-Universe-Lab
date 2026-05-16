package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement phoneInput = $("input[name='phone']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement loginButton = $("button[type='submit']");
    private final SelenideElement errorMessage = $(".error-text");
    private final SelenideElement loginTab = $("#loginTabBtn");
    private final SelenideElement registerTab = $("#registerTabBtn");
    private final SelenideElement regPhoneInput = $("#regPhone");
    private final SelenideElement regPasswordInput = $("#regPassword");
    private final SelenideElement regConfirmPasswordInput = $("#regConfirmPassword");
    private final SelenideElement registerButton = $("#registerBtn");
    private final SelenideElement registerMessage = $("#registerMessage");
    private final SelenideElement registerError = $("#registerError");
    private final SelenideElement regPhoneError = $("#regPhoneError");
    private final SelenideElement regPasswordError = $("#regPasswordError");
    private final SelenideElement regConfirmError = $("#regConfirmError");

    public void login(String phone, String password) {
        phoneInput.setValue(phone);
        passwordInput.setValue(password);
        loginButton.shouldBe(visible).click();
    }

    public void checkErrorMessage(String expectedText) {
        errorMessage.shouldBe(visible).shouldHave(text(expectedText));
    }

    public void shouldBeOnLoginPage() {
        loginTab.shouldBe(visible).shouldHave(text("Вход"));
    }

    public void switchToRegisterTab() {
        registerTab.shouldBe(visible).click();
    }

    public void register(String phone, String password, String confirmPassword) {
        regPhoneInput.setValue(phone);
        regPasswordInput.setValue(password);
        regConfirmPasswordInput.setValue(confirmPassword);
        registerButton.shouldBe(visible).click();
    }

    public void shouldSeeRegistrationSuccessMessage() {
        registerMessage.shouldBe(visible).shouldHave(text("Регистрация успешна! Теперь войдите."));
    }

    public void shouldSeeRegistrationErrorMessage(String expectedText) {
        registerError.shouldBe(visible).shouldHave(text(expectedText));
    }

    public void shouldSeePhoneError(String expectedText) {
        regPhoneError.shouldBe(visible).shouldHave(text(expectedText));
    }

    public void shouldSeePasswordError(String expectedText) {
        regPasswordError.shouldBe(visible).shouldHave(text(expectedText));
    }

    public void shouldSeeConfirmError(String expectedText) {
        regConfirmError.shouldBe(visible).shouldHave(text(expectedText));
    }
}