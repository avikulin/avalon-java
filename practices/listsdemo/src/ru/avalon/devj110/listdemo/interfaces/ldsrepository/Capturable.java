package ru.avalon.devj110.listdemo.interfaces.ldsrepository;

public interface Capturable<T> {
    void uptakeToHead(T obj) throws IllegalArgumentException;
    void uptakeToTail(T obj) throws IllegalArgumentException;
}
