package ar.edu.itba.pod.query2;

import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Collator;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class QueryCollator implements Collator<Map.Entry<Integer, Tuple<Long, Long>>, List<Map.Entry<Integer, Tuple<Long,Long>>>> {


    @Override
    public List<Map.Entry<Integer, Tuple<Long, Long>>> collate(Iterable<Map.Entry<Integer, Tuple<Long, Long>>> iterable) {
        return StreamSupport.stream(iterable.spliterator(),false)
                .sorted(new TupleComparator())
                .collect(java.util.stream.Collectors.toList());
    }

    private static class TupleComparator implements java.util.Comparator<Map.Entry<Integer, Tuple<Long,Long>>> {
        @Override
        public int compare(Map.Entry<Integer, Tuple<Long, Long>> o1, Map.Entry<Integer, Tuple<Long, Long>> o2) {
            var yearsComparison =  o2.getKey() - o1.getKey();
            return yearsComparison == 0 ? o1.getValue().getFirst().compareTo(o2.getValue().getFirst()) : yearsComparison;
        }
    }
}
