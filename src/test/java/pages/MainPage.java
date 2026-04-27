package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    public final SelenideElement header = $(".fixed-header h1");
    private final SelenideElement profileButton = $(".dropbtn");
    private final SelenideElement logoutLink = $(".dropdown-content a[href='/logout']");

    public void logout() {
        profileButton.click();
        logoutLink.shouldBe(visible);
        logoutLink.click();
    }
}

