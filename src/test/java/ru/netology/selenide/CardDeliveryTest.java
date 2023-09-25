package ru.netology.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    String meetingDay(int day) {
        return LocalDate.now().plusDays(day).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


    @ParameterizedTest
    @CsvSource({
            "Рязань, 4, Геннадий Заболотный, +79771231212",
            "Казань, 3, Василий Поддубный, +79770000000",
            "Казань, 20, Василий Поддубный-Меньшов, +79770000000",
            "Казань, 3, Василий Поддубный Второй, +79770000000"

    })
    public void testPositive(String city, int day, String name, String phone) {
        open("http://localhost:9999/");

        $("[data-test-id=city] input").setValue(city);

        String date = meetingDay(day);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(date);

        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=notification] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).shouldBe(exactText("Встреча успешно забронирована на " + date));
    }

    @ParameterizedTest
    @CsvSource({
            "Рязань, 3, Геннадий Заболотный, +79771231212"


    })
    public void testDefaultDate(String city, int day, String name, String phone) {
        open("http://localhost:9999/");


        $("[data-test-id=city] input").setValue(city);

        String date = meetingDay(day);

        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=notification] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).shouldBe(exactText("Встреча успешно забронирована на " + date));
    }

    @ParameterizedTest
    @CsvSource({
            "Ногинск, 3, Геннадий Заболотный, +79771231212, Доставка в выбранный город недоступна",
            "Noginsk, 3, Геннадий Заболотный, +79771231212, Доставка в выбранный город недоступна",
            ", 3, Геннадий Заболотный, +79771231212, Поле обязательно для заполнения"

    })
    public void testNegativeCity(String city, int day, String name, String phone, String message) {
        open("http://localhost:9999/");

        $("[data-test-id=city] input").setValue(city);

        String date = meetingDay(day);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(date);

        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city] .input__sub").shouldBe(exactText(message));
    }

    @ParameterizedTest
    @CsvSource({
            "Рязань, 0, Геннадий Заболотный, +79771231212, Заказ на выбранную дату невозможен",
            "Рязань, -1, Геннадий Заболотный, +79771231212, Заказ на выбранную дату невозможен",
            "Рязань, 2, Геннадий Заболотный, +79771231212, Заказ на выбранную дату невозможен"

    })
    public void testNegativeDate(String city, int day, String name, String phone, String message) {
        open("http://localhost:9999/");

        $("[data-test-id=city] input").setValue(city);

        String date = meetingDay(day);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(date);

        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input__sub").shouldBe(exactText(message));
    }

    @ParameterizedTest
    @CsvSource({
            "Рязань, Геннадий Заболотный, +79771231212, Неверно введена дата"

    })
    public void testEmptyDate(String city, String name, String phone, String message) {
        open("http://localhost:9999/");

        $("[data-test-id=city] input").setValue(city);

        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);

        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input__sub").shouldBe(exactText(message));
    }

    @ParameterizedTest
    @CsvSource({
            "Рязань, 4, 1111, +79771231212, 'Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.'",
            "Рязань, 4, , +79771231212, Поле обязательно для заполнения",
            "Рязань, 4, Vasilii, +79771231212, 'Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.'"

    })
    public void testNegaiveName(String city, int day, String name, String phone, String message) {
        open("http://localhost:9999/");

        $("[data-test-id=city] input").setValue(city);

        String date = meetingDay(day);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(date);

        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldBe(exactText(message));
    }

    @ParameterizedTest
    @CsvSource({
            "Рязань, 4, Василий Иванов, , Поле обязательно для заполнения",
            "Рязань, 4, Василй Иванов, 79771234567, 'Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.'",
            "Рязань, 4, Василй Иванов, 89771234567, 'Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.'",
            "Рязань, 4, Василй Иванов, +797712345, 'Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.'",
            "Рязань, 4, Василй Иванов, +797712345678, 'Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.'",
            "Рязань, 4, Василй Иванов, +7977-123-45-67, 'Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.'"

    })
    public void testNegaivePhone(String city, int day, String name, String phone, String message) {
        open("http://localhost:9999/");

        $("[data-test-id=city] input").setValue(city);

        String date = meetingDay(day);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(date);

        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldBe(exactText(message));
    }

    @ParameterizedTest
    @CsvSource({
            "Рязань, 4, Василй Иванов, +79771234567, Я соглашаюсь с условиями обработки и использования моих персональных данных"

    })
    public void testWithoutCheckbox(String city, int day, String name, String phone, String message) {
        open("http://localhost:9999/");

        $("[data-test-id=city] input").setValue(city);

        String date = meetingDay(day);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(date);

        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);

        $(".button").click();
        $("[data-test-id=agreement] .checkbox__text").shouldBe(exactText(message));
    }
}
