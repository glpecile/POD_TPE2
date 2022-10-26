package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.Month;
import lombok.Getter;

import java.io.Serializable;

public class QueryReading implements Serializable {
    @Getter
    private final int sensorId;
    @Getter
    private final Month month;
    @Getter
    private int count;

    public QueryReading(int sensorId, Month month, int count) {
        this.sensorId = sensorId;
        this.month = month;
        this.count = count;
    }

    public void incrementCount(int count) {
        this.count += count;
    }
}
