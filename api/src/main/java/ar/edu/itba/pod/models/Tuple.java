package ar.edu.itba.pod.models;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

public final class Tuple<T, K> implements Serializable {
    @Getter
    private final T first;
    @Getter
    private final K second;

    public Tuple(T first,K second) {
        this.first = first;
        this.second = second;
    }

    public T first() {
        return first;
    }

    public K second() {
        return second;
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
