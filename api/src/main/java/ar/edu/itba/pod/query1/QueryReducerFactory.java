package ar.edu.itba.pod.query1;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class QueryReducerFactory implements ReducerFactory<String, Long,Long> {
    @Override
    public Reducer<Long, Long> newReducer(String sensorName) {
        return new QueryReducer();
    }
    private static class QueryReducer extends Reducer<Long, Long> {
            private long sum;
            @Override
            public void beginReduce() {
                sum = 0;
            }
            @Override
            public void reduce(Long hourlyCount) {
                sum = sum + hourlyCount;
            }

            @Override
            public Long finalizeReduce() {
                return sum;
            }
        }
}
