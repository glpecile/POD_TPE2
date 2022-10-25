package ar.edu.itba.pod.query3;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class QueryReading implements Serializable {
    @Getter
    @Setter
    Integer id;
    @Getter
    @Setter
    Integer count;
    @Getter
    @Setter
    String date_time;

    public QueryReading(Integer id, Integer count, String date_time) {
        this.id = id;
        this.count = count;
        this.date_time = date_time;
    }
}

