package pl.szczep.app.parsers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public enum Booleans {
    FALSE,
    TRUE;

    public static List<String> stringValues() {
        return Arrays.stream(Booleans.values())
            .map(Enum::toString)
            .collect(Collectors.toList());
    }
}
