package ar.edu.itba.pod.client.query3;

import ar.edu.itba.pod.client.models.Arguments;
import ar.edu.itba.pod.client.query3.CliParser;
import ar.edu.itba.pod.client.utils.Hazelcast;
import ar.edu.itba.pod.client.utils.PerformanceTimer;
import ar.edu.itba.pod.csv.CsvHelper;
import ar.edu.itba.pod.models.Month;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.Tuple;
import ar.edu.itba.pod.query3.*;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.KeyValueSource;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
public class Client {

    private final static String HZ_READINGS_LIST = Optional
            .ofNullable(System.getenv("HZ_READINGS_LIST"))
            .orElse("g3-query3-readings-list");
    private final static String HZ_JOB_TRACKER = Optional
            .ofNullable(System.getenv("HZ_JOB_TRACKER"))
            .orElse("g3-query3");
    private final static String SENSORS_FILE_NAME = Optional
            .ofNullable(System.getenv("SENSORS_FILE_NAME"))
            .orElse("/sensors.csv");
    private final static String READINGS_FILE_NAME = Optional
            .ofNullable(System.getenv("READINGS_FILE_NAME"))
            .orElse("/readings.csv");
    private final static String EXPORT_FILE_NAME = Optional
            .ofNullable(System.getenv("EXPORT_FILE_NAME"))
            .orElse("/query3.csv");
    private final static String TIME_FILE_NAME = Optional
            .ofNullable(System.getenv("EXPORT_FILE_NAME"))
            .orElse("/time3.csv");


    public static void main(String[] args) {
        log.info("Query 3 client starting...");

        var parser = new CliParser();
        Optional<Arguments> arguments = parser.parse(args);

        if (arguments.isEmpty()) {
            log.error("Invalid arguments");
            return;
        }
        var inPath = arguments.get().getInPath();

        try {
            @Cleanup var timer = new PerformanceTimer(arguments.get().getOutPath() + TIME_FILE_NAME, log);

            timer.startLoadingDataFromFile();
            var sensors = CsvHelper.parseSensorFile(inPath + SENSORS_FILE_NAME)
                    .stream()
                    .collect(Collectors.toMap(Sensor::getId, t->t));
            var readings = CsvHelper.parseReadingsFile(inPath + READINGS_FILE_NAME)
                    .stream()
                    .map(t ->new QueryReading(t.getSensorId(), t.getHourlyCount(), String.format("%d/%s/%d %d:00", t.getDayOfTheMonth(),  MonthToNum(t.getMonth()), t.getYear(), t.getHourOfTheDay() )))
                    .toList();

            log.info("Read {} sensors and {} readings", sensors.size(), readings.size());
            timer.endLoadingDataFromFile();

            log.info("Starting Hazelcast client...");
            var hazelcast = Hazelcast.connect(arguments.get());
            log.info("Hazelcast client started!");

            timer.startLoadingDataToHazelcast();

            IList<QueryReading> readingsList = hazelcast.getList(HZ_READINGS_LIST);
            readingsList.clear();
            readingsList.addAll(readings);

            timer.endLoadingDataToHazelcast();

            var dataSource = KeyValueSource.fromList(readingsList);
            var jobTracker = hazelcast.getJobTracker(HZ_JOB_TRACKER);
            var job = jobTracker.newJob(dataSource);


            timer.startMapReduce();


            var arg = (CliParser.Arguments) arguments.get();
            var future = job
                    .mapper(new QueryMapper(sensors, arg.getMin()))
                    .combiner(new QueryCombinerFactory())
                    .reducer(new QueryReducerFactory())
                    .submit(new QueryCollator());

            List<Map.Entry<String, Tuple<Long, String>>> result = new ArrayList<>();
            result = future.get();

            CsvHelper.writeFile(
                        arguments.get().getOutPath() + EXPORT_FILE_NAME,
                        "Sensor;Total_Count",
                        result,
                        entry -> entry.getKey() + ";" + entry.getValue().getFirst()+ ";" + entry.getValue().getSecond()
            );






        } catch (IOException e) {
            log.error("The files 'sensors.csv' and 'readings.csv' are not in the specified inFolder: {}", arguments.get().getInPath());
        }

        catch (Exception e){
            log.error("Unexpected error: {}",e.getMessage(),e);
        }
        finally {
            // Shutdown
            HazelcastClient.shutdownAll();
        }
    }
    private static String MonthToNum(Month month){
        switch (month){
            case JANUARY:
                return "01";
            case FEBRUARY:
                return "02";
            case MARCH:
                return "03";
            case APRIL:
                return "04";
            case MAY:
                return "05";
            case JUNE:
                return "06";
            case JULY:
                return "07";
            case AUGUST:
                return "08";
            case SEPTEMBER:
                return "09";
            case OCTOBER:
                return "10";
            case NOVEMBER:
                return "11";
            case DECEMBER:
                return "12";
            default:
                return "-1";
        }
    }
}
