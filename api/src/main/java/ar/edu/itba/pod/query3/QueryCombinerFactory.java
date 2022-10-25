package ar.edu.itba.pod.query3;

import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class QueryCombinerFactory implements CombinerFactory<String, Tuple<Long, String>,Tuple<Long, String>> {
    @Override
    public Combiner<Tuple<Long, String>, Tuple<Long, String>> newCombiner(String integer) {
        return new QueryCombiner();
    }

    private static class QueryCombiner extends Combiner<Tuple<Long, String>, Tuple<Long, String>> {
        private Tuple<Long, String> max = new Tuple<>(0L,"");

        @Override
        public  void combine(Tuple<Long, String> integer) {
            if(max.getFirst()<integer.getFirst())
                max = integer;
        }
        
        @Override
        public void reset() {
            max = new Tuple<>(0L,"");;
        }
        
        @Override
        public Tuple<Long, String> finalizeChunk() {
            return max;
        }
    }
}
