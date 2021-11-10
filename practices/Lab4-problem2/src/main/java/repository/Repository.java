package repository;

import interfaces.BooleanRepository;

import java.util.Iterator;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class Repository implements BooleanRepository, Iterable<Boolean> {
    private boolean[] storage;

    public Repository(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size must be at least 1");
        }
        this.storage = new boolean[size];
    }

    public Repository(boolean[] source) {
        if ((source == null) || (source.length == 0)) {
            throw new IllegalArgumentException("Passed array reference must be not-null and points to non-empty array");
        }
        this.storage = source;
    }

    private void checkBounds(int idx) throws IllegalArgumentException {
        if ((idx < 0) || (idx > storage.length - 1)) {
            throw new IllegalArgumentException("Index is out of bounds");
        }
    }

    public boolean checkElement(int idx) throws IllegalArgumentException {
        checkBounds(idx);
        return storage[idx];
    }

    public void setElement(int idx) throws IllegalArgumentException {
        checkBounds(idx);
        storage[idx] = true;
    }

    public void put(int idx, boolean value) throws IllegalArgumentException {
        checkBounds(idx);
        storage[idx] = value;
    }

    public void unsetElement(int idx) throws IllegalArgumentException {
        checkBounds(idx);
        storage[idx] = false;
    }

    public void invertElement(int idx) throws IllegalArgumentException {
        checkBounds(idx);
        storage[idx] = !storage[idx];
    }

    public int countTrueElements() {
        int res = 0;
        for (boolean e : this) {
            res += e ? 1 : 0;
        }
        return res;
    }

    @Override
    public String toString() {
        StringJoiner res = new StringJoiner(", ", "[", "]");
        for (boolean e : this) {
            res.add(e ? "1" : "0");
        }
        return res.toString();
    }

    public Iterator<Boolean> iterator() {
        return new Iterator<Boolean>() {
            int currentPos = -1;

            @Override
            public boolean hasNext() {
                return currentPos < storage.length - 1;
            }

            @Override
            public Boolean next() {
                currentPos++;
                return storage[currentPos];
            }
        };
    }

    public void forEach(Consumer<? super Boolean> action) {
        for (boolean e : this) {
            action.accept(e);
        }
    }
}
