package models.repository;

import interfaces.Repository;
import interfaces.base.FileSystemObject;

import java.util.Iterator;

public class FileRepository<T extends FileSystemObject> implements Repository<T> {
    private final T[] storage;
    private int pointer;

    public FileRepository(T[] storage) throws IllegalStateException {
        if ((storage == null) || (storage.length == 0)) {
            throw new IllegalArgumentException("External storage reference must be not null");
        }
        this.storage = storage;
        this.pointer = -1;
    }

    @Override
    public void put(T element) throws IllegalStateException {
        if (element == null) {
            throw new IllegalArgumentException("Element reference must be not null");
        }

        if (pointer > storage.length - 1) {
            throw new IllegalStateException("There is no enough space to store element data");
        }
        pointer++;
        storage[pointer] = element;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int currentPosition = -1;

            @Override
            public boolean hasNext() {
                return currentPosition < pointer;
            }

            @Override
            public T next() {
                currentPosition++;
                return storage[currentPosition];
            }
        };
    }
}
