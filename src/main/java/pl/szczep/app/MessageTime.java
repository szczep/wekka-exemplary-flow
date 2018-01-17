package pl.szczep.app;

import java.time.LocalTime;

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
}
