package ar.edu.itba.pod.query5;

import ar.edu.itba.pod.models.Tuple;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

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
        var finalResults = new TreeMap<Long, List<Tuple<String,String>>>(Comparator.reverseOrder());
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

}
