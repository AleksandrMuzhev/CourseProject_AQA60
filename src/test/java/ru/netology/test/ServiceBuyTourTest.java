package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DataSql;
import ru.netology.page.PageTour;
import static com.codeborne.selenide.Selenide.open;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceBuyTourTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void clearDatabaseTables() {
        DataSql.clearTables();
    }

    @Test
    @DisplayName("Оплата тура с валидной картой с статусом APPROVED")
    public void testCashValidCardApproved() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.messageSuccess();
        assertEquals("APPROVED", DataSql.findPayStatus());
    }

    @Test
    @DisplayName("Кредит за тур с валидной картой с статусом APPROVED")
    public void testCreditValidCardApproved() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.messageSuccess();
        assertEquals("APPROVED", DataSql.findCreditStatus());
    }

    @Test
    @DisplayName("Оплата тура с валидной картой с статусом DECLINED")
    public void testCashValidCardDeclined() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberDeclined());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.messageError();
        assertEquals("DECLINED", DataSql.findPayStatus());
    }

    @Test
    @DisplayName("Кредит за тур с валидной картой с статусом DECLINED")
    public void testCreditValidCardDeclined() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberDeclined());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.messageError();
        assertEquals("DECLINED", DataSql.findCreditStatus());
    }

    @Test
    @DisplayName("Оплата тура по несуществующей карте")
    public void testCashInvalidCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberNothing());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.messageError();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по несуществующей карте")
    public void testCreditInvalidCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberNothing());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.messageError();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по не полностью заполненной карте")
    public void testCashCardNotFilled() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberNotFilled());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по не полностью заполненной карте")
    public void testCreditCardNotFilled() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberNotFilled());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с истекшим сроком действия (месяц)")
    public void testCashInvalidMonthCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonthOneMonthAgo());
        pageTour.setCardYear(DataHelper.getCurrentYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с истекшим сроком действия (месяц)")
    public void testCreditInvalidMonthCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonthOneMonthAgo());
        pageTour.setCardYear(DataHelper.getCurrentYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с невалидным месяцем 00")
    public void testCashInvalidMonthNullCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getInvalidMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с невалидным месяцем 00")
    public void testCreditInvalidMonthNullCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getInvalidMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с незаполненым полем месяц")
    public void testCashMonthEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getEmptyMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с незаполненым полем месяц")
    public void testCreditMonthEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getEmptyMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с истекшим сроком действия (год)")
    public void testCashInvalidYearCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getPreviousYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.cardExpired();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с истекшим сроком действия (год)")
    public void testCreditInvalidYearCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getPreviousYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.cardExpired();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с годом + 6 лет от текущего")
    public void testCashYearPlus6Card() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getCurrentYearPlus6());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с годом + 6 лет от текущего")
    public void testCreditYearPlus6Card() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getCurrentYearPlus6());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с незаполненным полем год")
    public void testCashYearEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getEmptyYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с незаполненным полем год")
    public void testCreditYearEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getEmptyYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с вводом цифр в поле Владелец")
    public void testCashNumberUserCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getNumberUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.requiredField();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с вводом специальных символов в поле Владелец")
    public void testCashSpecialUserCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getSpecialCharactersUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.requiredField();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с пустым полем Владелец")
    public void testCashUserEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getEmptyUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.requiredField();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с вводом цифр в поле Владелец")
    public void testCreditNumberUserCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getNumberUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.requiredField();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с вводом специальных символов поле Владелец")
    public void testCreditSpecialUserCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getSpecialCharactersUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.requiredField();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с пустым полем Владелец")
    public void testCreditUserEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getEmptyUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.requiredField();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с вводом одной цифры в поле CVC/CVV")
    public void testCashOneCvcCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.get1Cvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с вводом двух цифр в поле CVC/CVV")
    public void testCashTwoCvcCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.get2Cvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с пустым значением в поле CVC/CVV")
    public void testCashCvcEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getEmptyCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с вводом одной цифры в поле CVC/CVV")
    public void testCreditOneCvcCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.get1Cvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с вводом двух цифр в поле CVC/CVV")
    public void testCreditTwoCvcCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.get2Cvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с пустым значением в поле CVC/CVV")
    public void testCreditCvcEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getEmptyCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура с незаполнеными полями")
    public void testCashFieldsEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberEmpty());
        pageTour.setCardMonth(DataHelper.getEmptyMonth());
        pageTour.setCardYear(DataHelper.getEmptyYear());
        pageTour.setCardUser(DataHelper.getEmptyUser());
        pageTour.setCardCVC(DataHelper.getEmptyCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур с незаполнеными полями")
    public void testCreditFieldsEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberEmpty());
        pageTour.setCardMonth(DataHelper.getEmptyMonth());
        pageTour.setCardYear(DataHelper.getEmptyYear());
        pageTour.setCardUser(DataHelper.getEmptyUser());
        pageTour.setCardCVC(DataHelper.getEmptyCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по не заполненной карте")
    public void testCashEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberEmpty());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по не заполненной карте")
    public void testCreditEmptyCard() {
        var pageTour = open("http://localhost:8080", PageTour.class);
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberEmpty());
        pageTour.setCardMonth(DataHelper.getMonth());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.incorrectFormat();
        assertEquals(0, DataSql.getOrderEntityCount());
    }
}