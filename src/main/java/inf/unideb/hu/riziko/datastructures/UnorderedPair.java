package inf.unideb.hu.riziko.datastructures;

import java.util.Objects;

/**
 * Pár struktúra, ami az elemek sorrendjét nem veszi figyelembe.
 */
//TODO: tesztek, mivel a stackoverflowról és chatGPT-ről lett összetákolva xd
public class UnorderedPair<T> {
    private final T first;
    private final T second;

    public UnorderedPair(T first, T second) {
        if (first.hashCode() <= second.hashCode()) {
            this.first = first;
            this.second = second;
        }
        else {
            this.first = second;
            this.second = first;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnorderedPair<?> pair = (UnorderedPair<?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}