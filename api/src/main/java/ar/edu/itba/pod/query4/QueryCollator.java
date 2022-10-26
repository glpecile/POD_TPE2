package ar.edu.itba.pod.query4;

import ar.edu.itba.pod.models.Month;
import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class QueryCollator implements Collator<Map.Entry<String, Tuple<Month, Double>>, List<Map.Entry<String, Tuple<Month, Double>>>> {

    private final int n;

    public QueryCollator(int n) {
        this.n = n;
    }

    @Override
    public List<Map.Entry<String, Tuple<Month, Double>>> collate(Iterable<Map.Entry<String, Tuple<Month, Double>>> iterable) {
        return StreamSupport.stream(iterable.spliterator(),false)
                .sorted(new TupleComparator())
                .limit(n)
                .toList();
    }

    private static class TupleComparator implements Comparator<Map.Entry<String, Tuple<Month, Double>>> {
        @Override
        public int compare(Map.Entry<String, Tuple<Month, Double>> o1, Map.Entry<String, Tuple<Month, Double>> o2) {
            var numberComparison = o2.getValue().getSecond().compareTo(o1.getValue().getSecond());
            return numberComparison == 0 ? o1.getKey().compareTo(o2.getKey()) : numberComparison;
        }
    }
}
