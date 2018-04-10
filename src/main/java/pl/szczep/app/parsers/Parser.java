package pl.szczep.app.parsers;

import java.util.List;

/**
 * Place description here.
 *
 * @author Q1N9@nykredit.dk
 */

public interface Parser {
    List<String> mergeLinesWithNoTimeStamp(List<String> lines);
}
