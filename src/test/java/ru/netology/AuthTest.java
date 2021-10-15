package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.DataGenerator.Registration.*;


public class AuthTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSendActiveUser() {
        LogInfo activeUserInfo = DataGenerator.Registration.generateActiveUser();
        $("[name=login]").setValue(activeUserInfo.getLogin());
        $("[name=password]").setValue(activeUserInfo.getPassword());
        $(".button__text").click();
        $(byText("Личный кабинет")).shouldBe(exist);
    }

    @Test
    void shouldSendBlockedUser() {
        LogInfo blockedUserInfo = generateBlockedUser();
        $("[name=login]").setValue(blockedUserInfo.getLogin());
        $("[name=password]").setValue(blockedUserInfo.getPassword());
        $(".button__text").click();
        $(".notification_status_error").shouldBe(visible, Duration.ofMillis(6000));
        $(".notification_visible[data-test-id=error-notification]")
                .shouldHave(matchText("Ошибка! Пользователь заблокирован"));

    }

    @Test
    void shouldSendWrongPassword() {
        LogInfo wrongPassword = generateWrongPassword("active");
        $("[name=login]").setValue(wrongPassword.getLogin());
        $("[name=password]").setValue(wrongPassword.getPassword());
        $(".button__text").click();
        $(".notification_status_error").shouldBe(visible, Duration.ofMillis(5000));
        $(".notification_visible[data-test-id=error-notification]")
                .shouldHave(matchText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldSendWrongLogin() {
        LogInfo wrongLogin = generateWrongLogin("active");
        $("[name=login]").setValue(wrongLogin.getLogin());
        $("[name=password]").setValue(wrongLogin.getPassword());
        $(".button__text").click();
        $(".notification_status_error").shouldBe(visible, Duration.ofMillis(5000));
        $(".notification_visible[data-test-id=error-notification]")
                .shouldHave(matchText("Ошибка! Неверно указан логин или пароль"));
    }
}
