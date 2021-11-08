package ru.avalon.devj110.listdemo.interfaces.basedatastructures;

import ru.avalon.devj110.listdemo.dto.SegmentSearchResult;
import ru.avalon.devj110.listdemo.interfaces.dto.SearchResultsContainer;

import java.util.function.Consumer;

public interface LinearDataSegment<T> {
    boolean isEmpty();
    boolean isFull();
    int getLength();
    int getHeadPointer();
    int getTailPointer();
    int getFreeCellsLeft();
    int getFreeCellsRight();
    void pushFirst(T value) throws IllegalArgumentException, IllegalStateException;
    void pushLast(T value) throws IllegalArgumentException, IllegalStateException;
    T peekFirst() throws IllegalStateException;
    T peekLast() throws IllegalStateException;
    T popFirst() throws IllegalStateException;
    T popLast() throws IllegalStateException;
    void clearData();
    void fill(T[] values) throws IllegalArgumentException;
    SearchResultsContainer searchElement(T element) throws IllegalArgumentException;
    void removeElement(T element) throws IllegalArgumentException;
    void mapAction(Consumer<T> action) throws IllegalArgumentException;
    void setNextSegment(LinearDataSegment<T> obj) throws IllegalArgumentException;
    void clearNextSegment();
    LinearDataSegment<T> getNextSegment();
}
