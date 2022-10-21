package ar.edu.itba.pod.client.utils;

import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    
    public void startLoadingDataFromFile() throws IOException {
        logger.info("Reading csv files...");
        writeToFile("Inicio de la lectura del archivo");
    }
    
    public void endLoadingDataFromFile() throws IOException {
        logger.info("Reading csv files finished!");
        writeToFile("Fin de la lectura del archivo");
    }
    public void startLoadingDataToHazelcast() throws IOException {
        logger.info("Uploading data to hazelcast...");
        writeToFile("Inicio de la escritura del archivo a hazelcast");
    }
    
    public void endLoadingDataToHazelcast() throws IOException {
        logger.info("Data uploaded to hazelcast!");
        writeToFile("Fin de la escritura del archivo a hazelcast");
    }
    
    public void startMapReduce() throws IOException {
        logger.info("Starting map reduce...");
        writeToFile("Inicio del trabajo map/reduce");
    }
    
    public void endMapReduce() throws IOException {
        logger.info("Map reduce finished!");
        writeToFile("Fin del trabajo map/reduce");
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
