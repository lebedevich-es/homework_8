package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class WebTests {

    @ValueSource(strings = {
            "Планета нервных",
            "Заблудившийся автобус",
            "Йога для всех"})
    @ParameterizedTest(name = "Для запроса {0} открывается страница товара")
    void searchResultIsItemPageTest(String title) {
        open("https://oz.by/");
        $("#top-s").setValue(title).pressEnter();
        $(".b-product-title__heading h1").shouldHave(text(title));
    }

    @CsvSource(value = {
            "Дальше живите сами, По запросу «Дальше живите сами» ничего не найдено",
            "Забвение пахнет корицей, Найдено 2 товара по запросу «Забвение пахнет корицей»",
            "Цветы для Элджернона, Найдено 16 товаров по запросу «Цветы для Элджернона»"
    })
    @ParameterizedTest(name = "Для запроса {0} открывается страница с результатом поиска: {1}")
    void searchResultIsPageWithResultsTest(String title, String expectedResult) {
        open("https://oz.by/");
        $("#top-s").setValue(title).pressEnter();
        $("[itemprop='itemListElement']").shouldHave(text(expectedResult));
    }

    static Stream<Arguments> popularTopicsTest() {
        return Stream.of(
                Arguments.of("Дом, сад, зоотовары", List.of(
                        "Для кухни", "Бытовая химия, хозтовары", "Домашний текстиль", "Для интерьера", "Зоотовары")),
                Arguments.of("Книги", List.of(
                        "Бестселлеры", "Главные новинки", "Художественная литература", "Нехудожественная литература", "Фэнтези и фантастика")),
                Arguments.of("Косметика, парфюмерия", List.of(
                        "Косметика", "Парфюмерия", "Аксессуары", "Средства гигиены", "Техника для красоты")),
                Arguments.of("Развлечения, творчество", List.of(
                        "Картины по номерам", "Настольные игры", "Конструкторы", "Наборы для творчества", "Пазлы"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Для страницы {0}, популярные разделы: {1}")
    void popularTopicsTest(String menuItem, List<String> items) {
        open("https://oz.by/");
        $$(".main-nav__list__li_wnav").find(text(menuItem)).click();
        $$(".landing-nav-list__line-inner li").shouldHave(CollectionCondition.texts(items));
    }
}