package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class PageTour {
    private SelenideElement buyButton = $$(".button__text").find(exactText("Купить"));
    private SelenideElement buyCreditButton = $$(".button__text").find(exactText("Купить в кредит"));
    private SelenideElement cardNumberField = $$(".input__inner").findBy(text("Номер карты")).$(".input__control");
    private SelenideElement monthField = $$(".input__inner").findBy(text("Месяц")).$(".input__control");
    private SelenideElement yearField = $$(".input__inner").findBy(text("Год")).$(".input__control");
    private SelenideElement userField = $$(".input__inner").findBy(text("Владелец")).$(".input__control");
    private SelenideElement cvcField = $$(".input__inner").findBy(text("CVC/CVV")).$(".input__control");
    private SelenideElement payCard = $$(".heading").find(exactText("Оплата по карте"));
    private SelenideElement payCreditCard = $$(".heading").find(exactText("Кредит по данным карты"));
    private SelenideElement messageSuccess = $$(".notification__title").find(exactText("Успешно"));
    private SelenideElement messageError = $$(".notification__content").find(exactText("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement continueButton = $$(".button__content").find(text("Продолжить"));
    private SelenideElement cardExpired = $$("span.input__sub").find(exactText("Истёк срок действия карты"));
    private SelenideElement invalidCardExpirationDate = $$("span.input__sub").find(exactText("Неверно указан срок действия карты"));
    private SelenideElement incorrectFormat = $$("span.input__sub").find(exactText("Неверный формат"));
    private SelenideElement requiredField = $$(".input__inner span.input__sub").find(exactText("Поле обязательно для заполнения"));

    public void buyWithCash() {
        buyButton.click();
        payCard.shouldBe(visible);
    }

    public void buyInCredit() {
        buyCreditButton.click();
        payCreditCard.shouldBe(visible);
    }

    public void setCardNumber(String number) {
        cardNumberField.setValue(number);
    }

    public void setCardMonth(String month) {
        monthField.setValue(month);
    }

    public void setCardYear(String year) {
        yearField.setValue(year);
    }

    public void setCardUser(String user) {
        userField.setValue(user);
    }

    public void setCardCVC(String cvc) {
        cvcField.setValue(cvc);
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void messageSuccess() {
        messageSuccess.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void messageError() {
        messageError.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void cardExpired() {
        cardExpired.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void invalidCardExpirationDate() {
        invalidCardExpirationDate.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void incorrectFormat() {
        incorrectFormat.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void requiredField() {
        requiredField.shouldBe(visible, Duration.ofSeconds(15));
    }
}
