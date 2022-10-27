package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.SensorStatus;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class QueryMapper implements Mapper<String, QueryReading, String, QueryReading> {
    private final Map<Integer, String> sensors;

    public QueryMapper(Map<Integer, String> sensors) {
        this.sensors = sensors;
    }

    @Override
    public void map(String s, QueryReading queryReading, Context<String, QueryReading> context) {
        var sensorName = sensors.get(queryReading.getSensorId());
        context.emit(sensorName, queryReading);
    }
}
