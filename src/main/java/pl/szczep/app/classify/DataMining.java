package pl.szczep.app.classify;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import pl.szczep.app.Classify;
import pl.szczep.app.parsers.LogsAdjuster;


public class DataMining {

    public static List<String> readAndAdjustData() throws IOException, URISyntaxException {
        Path path = Paths.get(Objects.requireNonNull(Classify.class.getClassLoader()
            .getResource("lsd_data.txt")).toURI());

        return LogsAdjuster.apply(
            Files.readAllLines(path),
            LogsAdjuster::cutNewDayLines,
            LogsAdjuster::mergeLinesWithNoTimeStamp);
    }
}
