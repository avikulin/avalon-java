package ru.avalon.devj110.listdemo.dto;

import ru.avalon.devj110.listdemo.interfaces.dto.SearchResultsContainer;
import ru.avalon.devj110.listdemo.interfaces.general.Serializable;

import java.util.Iterator;
import java.util.StringJoiner;

public class RepositorySearchResult implements SearchResultsContainer<SegmentSearchResult>, Iterable<SegmentSearchResult>, Serializable {
    private Object[] offset;
    private int length;

    public RepositorySearchResult(int size){
        offset = new Object[size];
        length = 0;
    }

    @Override
    public void append(SegmentSearchResult offsets) {
        offset[length] = offsets;
        length++;
    }

    public boolean isEmpty(){
        for (SegmentSearchResult v: this){
            if(!v.isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<SegmentSearchResult> iterator() {
        return new Iterator<SegmentSearchResult>() {
            private int currentPos = -1;
            @Override
            public boolean hasNext() {
                return currentPos < length - 1;
            }

            @Override
            public SegmentSearchResult next() {
                currentPos++;
                return (SegmentSearchResult)offset[currentPos];
            }
        };
    }

    @Override
    public String toString() {
        if (isEmpty()){
            return "[]";
        }

        StringJoiner res = new StringJoiner(", ", "[", "]");
        for (SegmentSearchResult item: this){
            res.add(item.toString());
        }
        return res.toString();
    }
}
