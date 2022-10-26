package ar.edu.itba.pod.client.query5;

import ar.edu.itba.pod.client.utils.Hazelcast;
import ar.edu.itba.pod.client.utils.PerformanceTimer;
import ar.edu.itba.pod.csv.CsvHelper;
import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.Tuple;
import ar.edu.itba.pod.query5.QueryCollator;
import ar.edu.itba.pod.query5.QueryCombinerFactory;
import ar.edu.itba.pod.query5.QueryMapper;
import ar.edu.itba.pod.query5.QueryReducerFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.KeyValueSource;
import lombok.Cleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Client {

    private final static Logger logger = LoggerFactory.getLogger(Client.class);

    private final static String HZ_READINGS_LIST = Optional
            .ofNullable(System.getenv("HZ_READINGS_LIST"))
            .orElse("g3-query5-readings-list");
    private final static String HZ_JOB_TRACKER = Optional
            .ofNullable(System.getenv("HZ_JOB_TRACKER"))
            .orElse("g3-query5");
    private final static String SENSORS_FILE_NAME = Optional
            .ofNullable(System.getenv("SENSORS_FILE_NAME"))
            .orElse("/sensors.csv");
    private final static String READINGS_FILE_NAME = Optional
            .ofNullable(System.getenv("READINGS_FILE_NAME"))
            .orElse("/readings.csv");
    private final static String EXPORT_FILE_NAME = Optional
            .ofNullable(System.getenv("EXPORT_FILE_NAME"))
            .orElse("/query5.csv");
    private final static String TIME_FILE_NAME = Optional
            .ofNullable(System.getenv("EXPORT_FILE_NAME"))
            .orElse("/time5.csv");

    public static void main(String[] args) {
        logger.info("Query 5 client starting...");
        
        var parser = new CliParser();
        var arguments = parser.parse(args);
        
        if (arguments.isEmpty()) {
            logger.error("Invalid arguments");
            return;
        }

        var inPath = arguments.get().getInPath();
        
        try {
            @Cleanup var timer = new PerformanceTimer(arguments.get().getOutPath() + TIME_FILE_NAME,logger);
            timer.startLoadingDataFromFile();
            var sensors = CsvHelper.parseSensorFile(inPath + SENSORS_FILE_NAME).stream()
                    .collect(Collectors.toMap(Sensor::getId, sensor -> sensor));

            var readings = CsvHelper.parseReadingsFile(inPath + READINGS_FILE_NAME).stream()
                    .map(t -> new Tuple<>(t.getSensorId(), t.getHourlyCount()))
                    .toList();



            logger.info("Read {} sensors and {} readings", sensors.size(), readings.size());
            timer.endLoadingDataFromFile();

            logger.info("Starting Hazelcast client...");
            var hazelcast = Hazelcast.connect(arguments.get());
            logger.info("Hazelcast client started!");

            timer.startLoadingDataToHazelcast();

            IList<Tuple<Integer,Integer>> readingList = hazelcast.getList(HZ_READINGS_LIST);
            readingList.clear();
            readingList.addAll(readings);

            timer.endLoadingDataToHazelcast();

            var dataSource = KeyValueSource.fromList(readingList);
            var jobTracker = hazelcast.getJobTracker(HZ_JOB_TRACKER);
            var job = jobTracker.newJob(dataSource);

            timer.startMapReduce();

            var future = job
                    .mapper(new QueryMapper(sensors))
                    .combiner(new QueryCombinerFactory())
                    .reducer(new QueryReducerFactory())
                    .submit(new QueryCollator());
            var result = future.get();

            timer.endMapReduce();

            CsvHelper.writeFile(
                    arguments.get().getOutPath() + EXPORT_FILE_NAME,
                    "Group;Sensor A;Sensor B",
                    result
            );

        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // Shutdown
            HazelcastClient.shutdownAll();
        }
    }
}
