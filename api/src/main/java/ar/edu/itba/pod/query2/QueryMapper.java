package ar.edu.itba.pod.query2;

import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

// QueryReading
public class QueryMapper implements Mapper<String, QueryReading, Integer, Tuple<Boolean, Integer>> {

    @Override
    public void map(String s, QueryReading queryReading, Context<Integer, Tuple<Boolean, Integer>> context) {
        context.emit(queryReading.getYear(), new Tuple<>(queryReading.isWeekend(), queryReading.getCount()));
    }
}
