package ar.edu.itba.pod.csv;

import ar.edu.itba.pod.models.Reading;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.Tuple;
import lombok.Cleanup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class CsvHelper {
    
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
    
    public static <T> void writeFile(String path, String header, List<T> a, Function<T,String> serialize) throws IOException {
        @Cleanup var buffer = Files.newBufferedWriter(
                Path.of(path),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
        );

        buffer.write(header);
        for (var entry: a) {
            buffer.newLine();
            buffer.write(serialize.apply(entry));
        }
    }

    public static void writeFile(String path, String header, TreeMap<Long,List<Tuple<String,String>>> a) throws IOException {
        @Cleanup var buffer = Files.newBufferedWriter(
                Path.of(path),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
        );

        buffer.write(header);
        for (var entry: a.entrySet()) {

            for (var tuple: entry.getValue()) {
                buffer.newLine();
                buffer.write(String.format("%s;%s;%s", entry.getKey() * 1000000, tuple.getFirst(), tuple.getSecond()));
            }

        }
    }


}
