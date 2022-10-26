package ar.edu.itba.pod.query2;

import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class QueryReducerFactory implements ReducerFactory<Integer, Tuple<Boolean, Integer>, Tuple<Long, Long>> {


    @Override
    public Reducer<Tuple<Boolean, Integer>, Tuple<Long, Long>> newReducer(Integer integer) {
        return new QueryReducer();
    }

    private static class QueryReducer extends Reducer<Tuple<Boolean, Integer>, Tuple<Long, Long>> {
        private long sumWeekend;
        private long sumWeekday;

        @Override
        public void beginReduce() {
            sumWeekend = 0;
            sumWeekday = 0;
        }

        @Override
        public void reduce(Tuple<Boolean, Integer> booleanIntegerTuple) {
            if (booleanIntegerTuple.getFirst()) {
                sumWeekend = sumWeekend + booleanIntegerTuple.getSecond();
            } else {
                sumWeekday = sumWeekday + booleanIntegerTuple.getSecond();
            }
        }

        @Override
        public Tuple<Long, Long> finalizeReduce() {
            return new Tuple<>(sumWeekday, sumWeekend);
        }
    }
}
