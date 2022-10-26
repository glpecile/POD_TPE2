package ar.edu.itba.pod.client.query4;

import ar.edu.itba.pod.client.utils.Hazelcast;
import ar.edu.itba.pod.client.utils.PerformanceTimer;
import ar.edu.itba.pod.csv.CsvHelper;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.query4.QueryCollator;
import ar.edu.itba.pod.query4.QueryMapper;
import ar.edu.itba.pod.query4.QueryReading;
import ar.edu.itba.pod.query4.QueryReducerFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.KeyValueSource;
import lombok.Cleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Client {

    private final static Logger logger = LoggerFactory.getLogger(Client.class);

    private final static String HZ_READINGS_LIST = Optional
            .ofNullable(System.getenv("HZ_READINGS_LIST"))
            .orElse("g3-query4-readings-list");
    private final static String HZ_JOB_TRACKER = Optional
            .ofNullable(System.getenv("HZ_JOB_TRACKER"))
            .orElse("g3-query4");
    private final static String SENSORS_FILE_NAME = Optional
            .ofNullable(System.getenv("SENSORS_FILE_NAME"))
            .orElse("/sensors.csv");
    private final static String READINGS_FILE_NAME = Optional
            .ofNullable(System.getenv("READINGS_FILE_NAME"))
            .orElse("/readings.csv");
    private final static String EXPORT_FILE_NAME = Optional
            .ofNullable(System.getenv("EXPORT_FILE_NAME"))
            .orElse("/query4.csv");
    private final static String TIME_FILE_NAME = Optional
            .ofNullable(System.getenv("EXPORT_FILE_NAME"))
            .orElse("/time4.csv");

    public static void main(String[] args) {
        logger.info("Query 4 client starting...");

        var parser = new CliParser();
        var optionalArguments = parser.parse(args);

        if (optionalArguments.isEmpty()) {
            logger.error("Invalid arguments");
            return;
        }

        var arguments = (CliParser.Arguments) optionalArguments.get();

        var inPath = arguments.getInPath();

        try {
            @Cleanup var timer = new PerformanceTimer(arguments.getOutPath() + TIME_FILE_NAME, logger);

            timer.startLoadingDataFromFile();
            var sensors = CsvHelper.parseSensorFile(inPath + SENSORS_FILE_NAME)
                    .stream()
                    .collect(Collectors.toMap(Sensor::getId, t -> t));

            var readings = CsvHelper.parseReadingsFile(inPath + READINGS_FILE_NAME)
                    .stream()
                    .filter(r -> r.getYear() == arguments.getYear())
                    .map(r -> new QueryReading(r.getSensorId(), r.getMonth(), r.getHourlyCount()))
                    .toList();

            logger.info("Read {} sensors and {} readings", sensors.size(), readings.size());
            timer.endLoadingDataFromFile();

            logger.info("Starting Hazelcast client...");
            var hazelcast = Hazelcast.connect(arguments);
            logger.info("Hazelcast client started!");

            timer.startLoadingDataToHazelcast();

            IList<QueryReading> readingsList = hazelcast.getList(HZ_READINGS_LIST);
            readingsList.clear();
            readingsList.addAll(readings);

            timer.endLoadingDataToHazelcast();

            var dataSource = KeyValueSource.fromList(readingsList);
            var jobTracker = hazelcast.getJobTracker(HZ_JOB_TRACKER);
            var job = jobTracker.newJob(dataSource);

            timer.startMapReduce();

            var future = job
                    .mapper(new QueryMapper(sensors))
                    .reducer(new QueryReducerFactory())
                    .submit(new QueryCollator(arguments.getN()));

            var result = future.get();

            CsvHelper.writeFile(
                    arguments.getOutPath() + EXPORT_FILE_NAME,
                    "Sensor;Month;Max_Monthly_Avg",
                    result,
                    entry -> String.format(Locale.ROOT, "%s;%s;%.2f", entry.getKey(), entry.getValue().getFirst().getName(), entry.getValue().getSecond())
            );

            timer.endMapReduce();
        } catch (IOException e) {
            logger.error("The files 'sensors.csv' and 'readings.csv' are not in the specified inFolder: {}", arguments.getInPath());
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Hazelcast execution error {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
        } finally {
            // Shutdown
            HazelcastClient.shutdownAll();
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
