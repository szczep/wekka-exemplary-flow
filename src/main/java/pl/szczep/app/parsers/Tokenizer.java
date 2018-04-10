package pl.szczep.app.parsers;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Tokenizer {

    private static final int MAX_WORD_LENGTH = 20;

    public static List<String> extractWords(List<String> lines) {
        return lines.stream()
            .map(TextParser::extractMessage)
            .flatMap(line -> Arrays.stream(line.toLowerCase().split("\\s+")))
            .filter(word -> word.length() <= MAX_WORD_LENGTH)
            .map(Stemming::stem)
            .distinct()
            .collect(Collectors.toList());
    }
}
