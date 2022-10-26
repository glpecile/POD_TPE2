package ar.edu.itba.pod.query2;

import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class QueryCombinerFactory implements CombinerFactory<Integer, Tuple<Boolean, Integer>, Tuple<Long, Long>> {
    @Override
    public QueryCombiner newCombiner(Integer integer) {
        return new QueryCombiner();
    }

    private static class QueryCombiner extends Combiner<Tuple<Boolean, Integer>, Tuple<Long, Long>> {
        private long sumWeekend = 0;
        private long sumWeekday = 0;

        @Override
        public void combine(Tuple<Boolean, Integer> booleanIntegerTuple) {
            if (booleanIntegerTuple.getFirst()) {
                sumWeekend += booleanIntegerTuple.getSecond();
            } else {
                sumWeekday += booleanIntegerTuple.getSecond();
            }
        }

        @Override
        public void reset() {
            sumWeekend = 0;
            sumWeekday = 0;
        }

        @Override
        public Tuple<Long, Long> finalizeChunk() {
            return new Tuple<>(sumWeekday, sumWeekend);
        }
    }
}
