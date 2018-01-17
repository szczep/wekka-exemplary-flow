package pl.szczep.app.parsers;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classify
 */
public class Tokenizer {

    public static String extractSender(String line) {
        final Pattern pattern = Pattern.compile("\\] (.+?):");
        final Matcher matcher = pattern.matcher(line);
        return matcher.find() ? matcher.group(1) : "";
    }

    public static String extractMessage(String line) {
        final Pattern pattern = Pattern.compile("\\] (.+?): (.*)");
        final Matcher matcher = pattern.matcher(line);
        return matcher.find() ? matcher.group(2) : "";
    }

    public static LocalTime extractTime(String line) {

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("h:m a")
            .toFormatter();

        final Pattern pattern = Pattern.compile("\\[(.+?)\\]");
        final Matcher matcher = pattern.matcher(line);

        return matcher.find() ? LocalTime.parse(matcher.group(1), formatter) : LocalTime.now();
    }

    public static boolean isLineStartingWithTime(String line) {
        final Pattern pattern = Pattern.compile("^\\[(.+?)\\]");
        return pattern.matcher(line).find();
    }

    public static boolean isLineANewDayStamp(String line) {
        final Pattern pattern = Pattern.compile("^----.*----$");
        return pattern.matcher(line).find();
    }
}
