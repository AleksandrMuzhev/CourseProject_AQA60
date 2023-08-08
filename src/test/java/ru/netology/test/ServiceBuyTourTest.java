package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.DataSql;
import ru.netology.page.PageTour;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceBuyTourTest {
    PageTour pageTour = new PageTour();

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
        pageTour.buyWithCash();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonthOneMonthAgo());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Кредит за тур по карте с истекшим сроком действия (месяц)")
    public void testCreditInvalidMonthCard() {
        pageTour.buyInCredit();
        pageTour.setCardNumber(DataHelper.getCardNumberApproved());
        pageTour.setCardMonth(DataHelper.getMonthOneMonthAgo());
        pageTour.setCardYear(DataHelper.getYear());
        pageTour.setCardUser(DataHelper.getUser());
        pageTour.setCardCVC(DataHelper.getCvc());
        pageTour.clickContinueButton();
        pageTour.invalidCardExpirationDate();
        assertEquals(0, DataSql.getOrderEntityCount());
    }

    @Test
    @DisplayName("Оплата тура по карте с невалидным месяцем 00")
    public void testCashInvalidMonthNullCard() {
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