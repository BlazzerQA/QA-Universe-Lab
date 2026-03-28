package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class RoadmapPage {
    public final SelenideElement header = $(".header h1");
    public final SelenideElement logoutButton = $(".logout-btn");

    public void logout() {
        logoutButton.click();
    }
}
