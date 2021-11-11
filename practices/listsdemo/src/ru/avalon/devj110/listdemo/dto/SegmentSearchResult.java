package ru.avalon.devj110.listdemo.dto;

import ru.avalon.devj110.listdemo.interfaces.dto.SearchResultsContainer;
import ru.avalon.devj110.listdemo.interfaces.general.Serializable;

import java.util.Iterator;
import java.util.StringJoiner;

public class SegmentSearchResult implements SearchResultsContainer<Integer>, Iterable<Integer>, Serializable {
    private Object[] offset;
    private int length;

    public SegmentSearchResult(int size){
        offset = new Object[size];
        length = 0;
    }

    @Override
    public void append(Integer offsetValue) {
        offset[length] = offsetValue;
        length++;
    }

    public boolean isEmpty(){return length==0;}

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int currentPos = -1;
            @Override
            public boolean hasNext() {
                return currentPos < length - 1;
            }

            @Override
            public Integer next() {
                currentPos++;
                return (Integer) offset[currentPos];
            }
        };
    }

    @Override
    public String toString() {
        if (isEmpty()){
            return "[]";
        }

        StringJoiner res = new StringJoiner(", ", "[", "]");
        for (Integer item: this){
            res.add(item.toString());
        }
        return res.toString();
    }
}
