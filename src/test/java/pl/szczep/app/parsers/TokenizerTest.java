package pl.szczep.app.parsers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;

import org.junit.Test;

/**
 *
 */
public class TokenizerTest {


    @Test
    public void shouldExtractMessageSender() {
        String hipChatLine = "[7:54 PM] Krzysztof Pawlicki: Pikanterii dodaje fakt że już im to kurwa wysylalem, " +
            "na ich specjalne życzenie, w dodatku chyba 2x :D";

        assertThat(Tokenizer.extractSender(hipChatLine), is("Krzysztof Pawlicki"));
    }

    @Test
    public void shouldExtractMessage() {
        String hipChatLine = "[2:03 PM] Jarek Krochmalski: Pioneer Kuro :)";

        assertThat(Tokenizer.extractMessage(hipChatLine), is("Pioneer Kuro :)"));
    }

    @Test
    public void shouldExtractMessageTime() {
        String hipChatLine = "[11:19 PM] Wojtek Lobacz: @KrzysztofPawlicki ale czy to nie oznacza, " +
            "że możesz sobie kiedy chcesz odejść z nyka/prodaty skoro oni nie mają umowy?";

        assertThat(Tokenizer.extractTime(hipChatLine), is(LocalTime.of(23, 19)));
    }

    @Test
    public void shouldDetectTimeStamp() {
        String hipChatLine = "[10:07 AM] Krzysztof Pawlicki: jej";

        assertTrue(Tokenizer.isLineStartingWithTime(hipChatLine));
    }

    @Test
    public void shouldDetectNoTimeStamp() {
        String hipChatLine = "- wszyscy, którzy pracują nad projektami dla Nykredit ";

        assertFalse(Tokenizer.isLineStartingWithTime(hipChatLine));
    }

    @Test
    public void shouldDetectNewDayStamp() {
        String hipChatLine = "---- Thursday October 5, 2017 ----";

        assertTrue(Tokenizer.isLineANewDayStamp(hipChatLine));
    }

    @Test
    public void shouldDetecNoNewDayStamp() {
        String hipChatLine = "[3:53 PM] Mariusz Lewandowski: DEJ DWA";

        assertFalse(Tokenizer.isLineANewDayStamp(hipChatLine));
    }
}
