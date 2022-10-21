package ar.edu.itba.pod.query1;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class QueryReducerFactory implements ReducerFactory<String, Long,Long> {
    @Override
    public Reducer<Long, Long> newReducer(String integer) {
        return new QueryReducer();
    }
    private class QueryReducer extends Reducer<Long, Long> {
            private volatile long sum;
            @Override
            public void beginReduce() {
                sum = 0;
            }
            @Override
            public synchronized void reduce(Long integer) {
                sum = sum + integer;
            }

            @Override
            public Long finalizeReduce() {
                return sum;
            }
        }
}
