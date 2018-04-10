package pl.szczep.app.parsers;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 *
 */
public class LogsAdjusterTest {

    @Test
    public void shouldMergeLinesWithNoTimeStamp() {

        List<String> lines = Arrays.asList(
            "[10:38 AM] Krzysztof Janicki: Poszukiwany JS Ninja na stanowisko " +
                "Technical Lead w Krk. Brytyjska Firma. JS+ fajnie jak JAVA. kont na priv\n",
            "[10:38 AM] Krzysztof Janicki: Technical Lead do 30K, js Ninja\n",
            "30,000 zł"
        );

        List<String> parsedLines = LogsAdjuster.mergeLinesWithNoTimeStamp(lines);
        assertThat(parsedLines.size(), is(2));
        assertThat(parsedLines, hasItems(
            "[10:38 AM] Krzysztof Janicki: Poszukiwany JS Ninja na stanowisko " +
                "Technical Lead w Krk. Brytyjska Firma. JS+ fajnie jak JAVA. kont na priv\n",
            "[10:38 AM] Krzysztof Janicki: Technical Lead do 30K, js Ninja\n 30,000 zł"));
    }
}