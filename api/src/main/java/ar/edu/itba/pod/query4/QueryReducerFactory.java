package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.Month;
import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QueryReducerFactory implements ReducerFactory<String, QueryReading, Tuple<Month, Double>> {

    @Override
    public Reducer<QueryReading, Tuple<Month, Double>> newReducer(String s) {
        return new QueryReducer();
    }

    private static class QueryReducer extends Reducer<QueryReading, Tuple<Month, Double>> {
        private volatile Map<Month, Integer> countByMonth;

        @Override
        public void beginReduce() {
            countByMonth = new HashMap<>();
            Arrays.stream(Month.values()).forEach(month -> countByMonth.put(month, 0));
        }

        @Override
        public synchronized void reduce(QueryReading queryReading) {
            Month m = queryReading.getMonth();
            int count = queryReading.getCount() + countByMonth.get(m);
            countByMonth.put(m, count);
        }

        @Override
        public Tuple<Month, Double> finalizeReduce() {
            Map<Month, Double> avgByMonth = new HashMap<>();
            countByMonth.forEach((month, count) -> avgByMonth.put(month, (double)count / month.getDays()));

            Month maxMonth = null;
            double maxAvg = 0.0;
            for (Map.Entry<Month, Double> entry : avgByMonth.entrySet()) {
                if (entry.getValue() > maxAvg) {
                    maxAvg = entry.getValue();
                    maxMonth = entry.getKey();
                }
            }
            return new Tuple<>(maxMonth, maxAvg);
        }
    }
}
