package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Collator;

import java.util.*;
import java.util.stream.Collectors;

public class QueryCollator implements Collator<Map.Entry<String,Long>, TreeMap<Long, List<Tuple<String,String>>>> {

    private final TreeMap<Long, TreeSet<String>> results;



    public QueryCollator() {
        this.results = new TreeMap<>();
    }



    @Override
    public TreeMap<Long, List<Tuple<String,String>>> collate(Iterable<Map.Entry<String, Long>> iterable) {
        iterable.forEach(entry -> {
            var millionCategory = entry.getValue();
            var list = results.getOrDefault(millionCategory, new TreeSet<>(Comparator.naturalOrder()));
            if (millionCategory != 0) {
                list.add(entry.getKey());
                results.put(millionCategory, list);
            }
        });
        //convert treemap into treemap with List of tuples between every sensor of the same category
        var finalResults = new TreeMap<Long, List<Tuple<String,String>>>();
        //filter from results the values with more than one sensor
            results.entrySet().stream().filter(entry -> entry.getValue().size() > 1).forEach(entry -> {
            List<Tuple<String,String>> list = new ArrayList<>();
            for (int i = 0; i < entry.getValue().size(); i++) {
                for (int j = i + 1; j < entry.getValue().size() ; j++) {
                    list.add(new Tuple<>(entry.getValue().toArray()[i].toString(), entry.getValue().toArray()[j].toString()));
                }
            }
            finalResults.put(entry.getKey(), list);
        });

        return finalResults;
    }

    static class AuxTuple<F,S> extends Tuple<F,S> {
        public AuxTuple(F first, S second) {
            super(first, second);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (AuxTuple) obj;
            return (Objects.equals(this.getFirst(), that.getFirst()) && Objects.equals(this.getSecond(), that.getSecond())) ||
                    (Objects.equals(this.getFirst(), that.getSecond()) && Objects.equals(this.getSecond(), that.getFirst()));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        private static class TupleComparator implements java.util.Comparator<AuxTuple<String,String>> {
            @Override
            public int compare(AuxTuple<String, String> o1, AuxTuple<String, String> o2) {
                //compare the first element with the first and second element of the tuple

                var firstComparison = o1.getFirst().compareTo(o2.getFirst()) ;
                return firstComparison == 0 ? o1.getSecond().compareTo(o2.getSecond()) : firstComparison;
            }
        }
    }
}
