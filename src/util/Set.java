package util;

public class Set<T> extends LinkedList<T>{
    @Override
    public void addFirst(T value) {
        if (!this.exists(value)) {
            super.addFirst(value);
        }
    }

    @Override
    public void addLast(T value) {
        if (!this.exists(value)) {
            super.addLast(value);
        }
    }
}
