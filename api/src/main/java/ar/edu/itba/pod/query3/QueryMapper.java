package ar.edu.itba.pod.query3;

import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.SensorStatus;
import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class QueryMapper implements Mapper<String,QueryReading,String,Tuple<Long, String>> {
    private final Map<Integer, String> sensors;


    public QueryMapper(Map<Integer, String> sensors) {
        this.sensors = sensors;
    }

    public void map(String s, QueryReading reading, Context<String,Tuple<Long, String>> context) {
        var sensorName = sensors.get(reading.getId());
        context.emit(sensorName,new Tuple<>(Long.valueOf(reading.getCount()), reading.getDate_time()));
    }


}
