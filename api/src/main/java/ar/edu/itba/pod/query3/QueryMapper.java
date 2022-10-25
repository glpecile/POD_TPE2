package ar.edu.itba.pod.query3;

import ar.edu.itba.pod.models.Sensor;
import ar.edu.itba.pod.models.SensorStatus;
import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Map;

public class QueryMapper implements Mapper<String,QueryReading,String,Tuple<Long, String>> {
    private final Map<Integer, Sensor> sensors;

    private int minPedestrians;

    public QueryMapper(Map<Integer, Sensor> sensors, int minPedestrians) {
        this.sensors = sensors;
        this.minPedestrians = minPedestrians;
    }

    public void map(String s, QueryReading reading, Context<String,Tuple<Long, String>> context) {
        var sensor = sensors.get(reading.getId());
        if (sensor.getStatus() == SensorStatus.ACTIVE && reading.getCount() >= minPedestrians)
            context.emit(sensor.getName(),new Tuple<>(Long.valueOf(reading.getCount()), reading.getDate_time()));
    }


}
