package ru.avalon.devj110.listdemo.interfaces.dto;

public interface SearchResultsContainer<T> {
    void append(T offset);
    boolean isEmpty();
}
