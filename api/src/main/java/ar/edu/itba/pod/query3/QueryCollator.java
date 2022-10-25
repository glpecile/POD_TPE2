package ar.edu.itba.pod.query3;

import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Collator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class QueryCollator implements Collator<Map.Entry<String, Tuple<Long, String>>, List<Map.Entry<String, Tuple<Long, String>>>> {

    @Override
    public List<Map.Entry<String, Tuple<Long, String>>> collate(Iterable<Map.Entry<String, Tuple<Long, String>>> iterable) {
        return StreamSupport.stream(iterable.spliterator(),false)
                .sorted(new TupleComparator())
                .collect(Collectors.toList());
        
    }

    private static class TupleComparator implements java.util.Comparator<Map.Entry<String,Tuple<Long, String>>> {
        @Override
        public int compare(Map.Entry<String, Tuple<Long, String>> o1, Map.Entry<String, Tuple<Long, String>> o2) {
            var numberComparison = o2.getValue().getFirst().compareTo(o1.getValue().getFirst());
            return numberComparison == 0 ? o1.getKey().compareTo(o2.getKey()) : numberComparison;
        }
    }
    
}
