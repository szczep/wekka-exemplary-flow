package pl.szczep.app.parsers;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public enum MessageTime {
    BEFORE_NINE,
    WORKING_HOURS,
    AFTER_SEVENTEEN;

    public static MessageTime createMessageTime(LocalTime time) {
        if (time.isBefore(LocalTime.of(9, 0))) {
            return BEFORE_NINE;
        } else if (time.isAfter(LocalTime.of(17, 0))) {
            return AFTER_SEVENTEEN;
        } else {
            return WORKING_HOURS;
        }
    }

    public static List<String> stringValues() {
        return Arrays.stream(MessageTime.values())
            .map(Enum::toString)
            .collect(Collectors.toList());
    }
}
