package com.kh.tong.util;

public class Pair<K, V> {
    private K first;
    private V second;

    public Pair() {
        this(null, null);
    }

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Pair [first=" + first + ", second=" + second + "]";
    }

}
