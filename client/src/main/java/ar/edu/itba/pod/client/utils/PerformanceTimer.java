package ar.edu.itba.pod.client.utils;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class PerformanceTimer implements Closeable {
    private final BufferedWriter buffer;
    private final Logger logger;

    public PerformanceTimer(String path, Logger logger) throws IOException {
        buffer = Files.newBufferedWriter(
                Path.of(path), 
                StandardOpenOption.CREATE, 
                StandardOpenOption.TRUNCATE_EXISTING, 
                StandardOpenOption.WRITE
        );
        this.logger = logger;
    }
    
    private StopWatch timer;
    
    public void startLoadingDataFromFile() throws IOException {
        logger.info("Reading csv files...");
        writeToFile("Inicio de la lectura del archivo");
        timer = new StopWatch();
        timer.start();
    }
    
    public void endLoadingDataFromFile() throws IOException {
        logger.info("Reading csv files finished!");
        writeToFile("Fin de la lectura del archivo");
        timer.stop();
        logger.info("Time elapsed loading from file: {} ms", timer.getTime());
    }
    public void startLoadingDataToHazelcast() throws IOException {
        logger.info("Uploading data to hazelcast...");
        writeToFile("Inicio de la escritura del archivo a hazelcast");
        timer = new StopWatch();
        timer.start();
    }
    
    public void endLoadingDataToHazelcast() throws IOException {
        logger.info("Data uploaded to hazelcast!");
        writeToFile("Fin de la escritura del archivo a hazelcast");
        timer.stop();
        logger.info("Time elapsed uploading to hazelcast: {} ms", timer.getTime());
    }
    
    public void startMapReduce() throws IOException {
        logger.info("Starting map reduce...");
        writeToFile("Inicio del trabajo map/reduce");
        timer = new StopWatch();
        timer.start();
    }
    
    public void endMapReduce() throws IOException {
        logger.info("Map reduce finished!");
        writeToFile("Fin del trabajo map/reduce");
        timer.stop();
        logger.info("Time elapsed map reduce: {} ms", timer.getTime());
    }
    
    private void writeToFile(String msg) throws IOException {
        String timestamp = (new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:SSSS")).format(new Date());
        buffer.write(String.format("%s - %s%n", timestamp, msg));
    }

    @Override
    public void close() throws IOException {
        buffer.close();
    }
}
