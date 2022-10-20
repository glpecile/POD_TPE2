package ar.edu.itba.pod.csv;

import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvReader {
    
    public static List<Sensor> parseSensorFile(String path) throws IOException {
        return Files.readAllLines(Path.of(path))
                .stream()
                .skip(1)
                .map(Sensor::new)
                .toList();
    }
    
    public static List<Reading> parseReadingsFile(String path) throws IOException {
        return Files.readAllLines(Path.of(path))
                .stream()
                .skip(1)
                .map(Reading::new)
                .toList();
    }
}
