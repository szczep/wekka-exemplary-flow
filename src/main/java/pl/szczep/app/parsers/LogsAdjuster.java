package pl.szczep.app.parsers;


import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


public class LogsAdjuster {

    @SafeVarargs
    public static <T> T apply(T initialValue, UnaryOperator<T>... unaryOperators) {

        T parsedValue = initialValue;
        for (UnaryOperator<T> operation : unaryOperators) {
            parsedValue = operation.apply(parsedValue);
        }

        return parsedValue;
    }

    public static List<String> mergeLinesWithNoTimeStamp(List<String> lines) {
        List<String> parsedLines = new LinkedList<>();

        String lastLine = lines.get(0);
        for (int i = 1; i < lines.size(); i++) {
            String currentLine = lines.get(i);

            if (!TextParser.isLineStartingWithTime(currentLine)) {
                lastLine = lastLine + " " + currentLine;
                if (isLastLine(lines, i)) {
                    parsedLines.add(lastLine);
                }
            } else {
                parsedLines.add(lastLine);
                lastLine = currentLine;
            }
        }
        return parsedLines;
    }

    public static List<String> cutNewDayLines(List<String> lines) {
        return lines.stream()
            .filter(line -> !TextParser.isLineANewDayStamp(line))
            .collect(Collectors.toList());
    }

    private static boolean isLastLine(List<String> lines, int i) {
        return i == lines.size() - 1;
    }
}
