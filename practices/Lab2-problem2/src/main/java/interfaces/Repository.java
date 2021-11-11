package interfaces;

import interfaces.base.FileSystemObject;

public interface Repository<T extends FileSystemObject> extends Iterable<T>{
    void put(T element) throws IllegalArgumentException;
}
