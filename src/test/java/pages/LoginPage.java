package pages;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement phoneInput = $("input[name='phone']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement loginButton = $("button[type='submit']");
    private final SelenideElement errorMessage = $(".error-text");

    public void login(String phone, String password) {
        phoneInput.setValue(phone);
        passwordInput.setValue(password);
        loginButton.click();
    }

    public void checkErrorMessage(String expectedText) {
        errorMessage.shouldBe(visible).shouldHave(text(expectedText));
    }
}