package ui;

import core.UiBaseTest;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginUiTest extends UiBaseTest {

    @Test
    public void testFullUserJourney() {

        //1. Открываем страничку
        open("/login");

        //2. Проверяем, что это она) по заголовку
        $("h1").shouldHave(text("QA Universe Auth"));

        //3. Вводим успешные данные
        $("input[name='phone']").setValue("+79991112233");
        $("input[name='password']").setValue("qwerty");

        //4. Входим по кнопке
        $("button").click();

        //5. Проверяем, что находимся на нужной странице
        $(".header h1").shouldHave(text("Roadmap: QA Automation"));
        $(".container").shouldHave(text("Java Collections"));

        //6. Выходим по кнопке "Выйти"
        $(".logout-btn").click();

        //7. Проверяем, что вышли - просто если видим кнопку LOGIN
        $("button").shouldBe(visible);

    }
}
