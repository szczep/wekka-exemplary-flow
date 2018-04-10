package pl.szczep.app.parsers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;

/**
 *
 */
public class TextParserTest {


    @Test
    public void shouldExtractMessageSender() {
        String hipChatLine = "[7:54 PM] Krzysztof Janicki: Pikanterii dodaje fakt że już im to kurwa wysylalem, " +
            "na ich specjalne życzenie, w dodatku chyba 2x :D";

        assertThat(TextParser.extractSender(hipChatLine), is("Krzysztof Janicki"));
    }

    @Test
    public void shouldExtractMessage() {
        String hipChatLine = "[2:03 PM] Jarek Makarski: Pioneer Kuro :)";

        assertThat(TextParser.extractMessage(hipChatLine), is("Pioneer Kuro :)"));
    }

    @Test
    public void shouldExtractMessageTime() {
        String hipChatLine = "[11:19 PM] Wojtek Obacz: @KrzysztofJanicki ale czy to nie oznacza, " +
            "że możesz sobie kiedy chcesz odejść z nyka/prodaty skoro oni nie mają umowy?";

        assertThat(TextParser.extractTime(hipChatLine), is(LocalTime.of(23, 19)));
    }

    @Test
    public void shouldDetectTimeStamp() {
        String hipChatLine = "[10:07 AM] Krzysztof Janicki: jej";

        assertTrue(TextParser.isLineStartingWithTime(hipChatLine));
    }

    @Test
    public void shouldDetectNoTimeStamp() {
        String hipChatLine = "- wszyscy, którzy pracują nad projektami dla Nykredit ";

        assertFalse(TextParser.isLineStartingWithTime(hipChatLine));
    }

    @Test
    public void shouldDetectNewDayStamp() {
        String hipChatLine = "---- Thursday October 5, 2017 ----";

        assertTrue(TextParser.isLineANewDayStamp(hipChatLine));
    }

    @Test
    public void shouldDetecNoNewDayStamp() {
        String hipChatLine = "[3:53 PM] Mariusz Ojcowski: DEJ DWA";

        assertFalse(TextParser.isLineANewDayStamp(hipChatLine));
    }

    @Test
    public void shouldFindWordStringInLine() {
        String hipChatLine = "[9:29 AM] Piotr Baratym: jak wymasterowac rozmowe kwalifikacyjna";

        assertTrue(TextParser.isAnyStringIncluded(hipChatLine, new HashSet<>(Arrays.asList("rozmowę", "Rozmowe"))));
    }

    @Test
    public void shouldNotFindWordStringInLine() {
        String hipChatLine = "[9:29 AM] Piotr Baratym: jak wymasterowac rozmowe kwalifikacyjna";

        assertFalse(TextParser.isAnyStringIncluded(hipChatLine, new HashSet<>(Collections.singletonList("rozmowę"))));
    }

    @Test
    public void shouldFindWord() {
        String hipChatLine = "[10:57 AM] Jaroslaw Korek: więc pewnie w podobnych kwocie bym miał. aż zapytam.";

        assertTrue(TextParser.isAnyWordIncluded(hipChatLine, new HashSet<>(Collections.singletonList("kwota"))));
    }

    @Test
    public void shouldNotFindWord() {
        String hipChatLine = "[9:29 AM] Piotr Baratym: jak wymasterowac rozmowe kwalifikacyjna";

        assertFalse(TextParser.isAnyWordIncluded(hipChatLine, new HashSet<>(Collections.singletonList("kot"))));
    }
}
