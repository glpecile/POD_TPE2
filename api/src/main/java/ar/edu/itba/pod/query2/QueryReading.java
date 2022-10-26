package ar.edu.itba.pod.query2;

import ar.edu.itba.pod.models.Day;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

public class QueryReading implements Serializable {
    @Getter
    private final int year;
    private final boolean isWeekend;
    @Getter
    private final int count;

    public QueryReading(int year, Day day, int count) {
        this.year = year;
        this.isWeekend = day == Day.SATURDAY || day == Day.SUNDAY;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryReading that = (QueryReading) o;
        return year == that.year && isWeekend == that.isWeekend;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, isWeekend);
    }

    public boolean isWeekend() {
        return isWeekend;
    }
}
