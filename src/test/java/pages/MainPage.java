package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    public final SelenideElement header = $("h1");
    public final SelenideElement logoutButton = $(".dropdown-content a[href='/logout']");

    public void logout() {
        logoutButton.click();
    }
}

