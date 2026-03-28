package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement phoneInput = $("input[name='phone']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement loginButton = $("button");
    private final SelenideElement errorMessage = $("p[style*='color: #ff4c4c']");

    public void login(String phone, String password) {
        phoneInput.setValue(phone);
        passwordInput.setValue(password);
        loginButton.click();
    }

    public String getErrorMessage() {
        return errorMessage.text();
    }

    public void checkErrorMessage(String expectedText) {
        errorMessage.shouldBe(visible).shouldHave(text(expectedText));
    }


}
