package pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static java.time.Duration.ofSeconds;

public class MainPage {

    public final SelenideElement header = $(".fixed-header h1");
    private final SelenideElement profileButton = $(".dropbtn");
    private final SelenideElement logoutLink = $(".dropdown-content a[href='/logout']");

    public void logout() {
        profileButton.click();
        logoutLink.shouldBe(visible, ofSeconds(3));
        logoutLink.click();
    }
}

