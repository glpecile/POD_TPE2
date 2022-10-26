package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.SensorStatus;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class QueryMapper implements Mapper<String, QueryReading, String, QueryReading> {
    private final Map<Integer, Sensor> sensors;

    public QueryMapper(Map<Integer, Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public void map(String s, QueryReading queryReading, Context<String, QueryReading> context) {
        var sensor = sensors.get(queryReading.getSensorId());
        if (sensor.getStatus() == SensorStatus.ACTIVE) {
            context.emit(sensor.getName(), queryReading);
        }
    }
}
