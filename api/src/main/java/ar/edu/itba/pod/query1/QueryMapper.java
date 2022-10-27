package ar.edu.itba.pod.query1;

import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class QueryMapper implements Mapper<String, Tuple<Integer,Integer>,String,Long> {
    private final Map<Integer, String> sensors;

    public QueryMapper(Map<Integer, String> sensors) {
        this.sensors = sensors;
    }

    @Override
    public void map(String listName, Tuple<Integer,Integer> readingValue, Context<String, Long> context) {
        var sensorName = sensors.get(readingValue.getFirst());
        context.emit(sensorName, Long.valueOf(readingValue.getSecond()));
    }
}
