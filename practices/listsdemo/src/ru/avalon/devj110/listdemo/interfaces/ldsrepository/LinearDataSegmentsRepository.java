package ru.avalon.devj110.listdemo.interfaces.ldsrepository;

import ru.avalon.devj110.listdemo.interfaces.dto.SearchResultsContainer;
import ru.avalon.devj110.listdemo.interfaces.general.Dumpable;
import ru.avalon.devj110.listdemo.interfaces.general.Serializable;
import ru.avalon.devj110.listdemo.repository.ListRepository;

import java.util.function.Consumer;

public interface LinearDataSegmentsRepository<T> {
    int getNumberOfSegments();
    void append(Iterable<T> collection) throws IllegalArgumentException;
    void pushFirst(T value) throws IllegalArgumentException;
    void pushFirst(T[] values) throws IllegalArgumentException;
    void pushFirst(Iterable<T> collection) throws IllegalArgumentException;
    void pushLast(T value) throws IllegalArgumentException;
    void pushLast(T[] values) throws IllegalArgumentException;
    void pushLast(Iterable<T> collection)throws IllegalArgumentException ;
    T peekFirst() throws IllegalStateException;
    T peekLast() throws IllegalStateException;
    T popFirst() throws IllegalStateException;
    T popLast() throws IllegalStateException;
    SearchResultsContainer searchElement(T element) throws IllegalArgumentException;
    void removeElement(T element) throws IllegalArgumentException;
    boolean isEmpty();
    void mapAction(Consumer<T> action) throws IllegalArgumentException;
    void performCompaction();
    void performNormalization();
}
