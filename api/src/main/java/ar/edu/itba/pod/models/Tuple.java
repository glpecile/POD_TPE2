package ar.edu.itba.pod.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

public class Tuple<T, K> implements Serializable {
    @Getter
    @Setter
    private T first;
    @Getter
    @Setter
    private K second;

    public Tuple() {
    }

    public Tuple(T first, K second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Tuple) obj;
        return Objects.equals(this.first, that.first) &&
                Objects.equals(this.second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "Tuple[" +
                "first=" + first + ", " +
                "second=" + second + ']';
    }
}
