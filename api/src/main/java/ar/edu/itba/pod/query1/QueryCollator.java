package ar.edu.itba.pod.query1;

import com.hazelcast.mapreduce.Collator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class QueryCollator implements Collator<Map.Entry<String,Long>, List<Map.Entry<String, Long>>> {

    @Override
    public List<Map.Entry<String, Long>> collate(Iterable<Map.Entry<String, Long>> iterable) {
        return StreamSupport.stream(iterable.spliterator(),false)
                .sorted(new TupleComparator())
                .collect(Collectors.toList());
        
    }

    private static class TupleComparator implements java.util.Comparator<Map.Entry<String,Long>> {
        @Override
        public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
            var numberComparison = o2.getValue().compareTo(o1.getValue());
            return numberComparison == 0 ? o1.getKey().compareTo(o2.getKey()) : numberComparison;
        }
    }
    
}
