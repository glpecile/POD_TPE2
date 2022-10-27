package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.SensorStatus;
import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class QueryMapper implements Mapper<String, Tuple<Integer, Integer>, String, Long> {

    private final Map<Integer, String> sensors;

    public QueryMapper(Map<Integer, String> sensors) {
        this.sensors = sensors;
    }

    @Override
    public void map(String s, Tuple<Integer, Integer> reading, Context<String, Long> context) {
        var sensorName = sensors.get(reading.getFirst());
        context.emit(sensorName, Long.valueOf(reading.getSecond()));
    }
}
