package ar.edu.itba.pod.query3;

import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class QueryReducerFactory implements ReducerFactory<String, Tuple<Long, String>,Tuple<Long, String>> {
    @Override
    public Reducer<Tuple<Long, String>, Tuple<Long, String>> newReducer(String integer) {
        return new QueryReducer();
    }
    private class QueryReducer extends Reducer<Tuple<Long, String>, Tuple<Long, String>> {
            private volatile Tuple<Long, String> max;
            @Override
            public void beginReduce() {
                max = new Tuple<>(0L, "");
            }
            @Override
            public synchronized void reduce(Tuple<Long, String> integer) {
                if(max.getFirst() < integer.getFirst())
                    max = integer;
            }

            @Override
            public Tuple<Long, String> finalizeReduce() {
                return max;
            }
        }
}
