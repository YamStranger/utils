package general;

/**
 * User: YamStranger
 * Date: 5/16/15
 * Time: 10:49 AM
 */
public class Pair<V extends Comparable, K extends Comparable> implements Comparable<Pair<V, K>> {
    @Override
    public int compareTo(Pair<V, K> o) {
        int c = this.right().compareTo(o.right());
        if (c == 0) {
            return this.left().compareTo(o.right());
        } else {
            return c;
        }
    }

    private final V v;
    private final K k;

    public Pair(V v, K k) {
        this.v = v;
        this.k = k;
    }

    public V left() {
        return v;
    }

    public K right() {
        return k;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (k != null ? !k.equals(pair.k) : pair.k != null) return false;
        if (v != null ? !v.equals(pair.v) : pair.v != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = v != null ? v.hashCode() : 0;
        result = 31 * result + (k != null ? k.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return v + " - " + k;
    }
}
