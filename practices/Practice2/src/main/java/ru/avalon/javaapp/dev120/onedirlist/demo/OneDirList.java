package ru.avalon.javaapp.dev120.onedirlist.demo;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OneDirList<T> implements Iterable<T>{
    private ListItem<T> head;
    private ListItem<T> tail;
    private ListItem<T> start;

    public void addToHead(T value) {
        if(head != null) {
            ListItem newItem = new ListItem<>(value);
            newItem.next = head;
            head = start = newItem;
        } else
            head = tail = start = new ListItem<>(value);
    }
    
    public T peekFromHead() {
        return head != null ? head.value : null;
    }
    
    public T removeFromHead() {
        if(head == null)
            return null;
        T res = head.value;
        if(head != tail)
            head = head.next;
        else
            head = tail = null;
        return res;
    }
    
    public void addToTail(T value) {
        if(tail != null) {
            tail.next = new ListItem(value);
            tail = tail.next;
        } else
            head = tail = new ListItem(value);
    }
    
    public T peekFromTail() {
        return tail != null ? tail.value : null;
    }
    
    public T removeFromTail() {
        if(tail == null)
            return null;
        
        T res = tail.value;
        if(head != tail) {
            ListItem it = head;
            while(it.next != tail)
                it = it.next;
            tail = it;
            tail.next = null;
        } else
            head = tail = null;
        return res;
    }
    
    public boolean contains(T value) {
        ListItem it = head;
        while(it != null) {
            if(it.checkValue(value))
                return true;
            it = it.next;
        }
        return false;
    }

    private ListItem<T> findItem(T value){
        ListItem<T> it = head;
        while (it !=null){
            if (it.checkValue(value)){
                return it;
            }
            it = it.next;
        }
        return null;
    }

    public boolean isEmpty() {
        return head == null;
    }
    
    public void printAll() {
        ListItem<T> it = head;
        while(it != null) {
            System.out.println(it.value);
            it = it.next;
        }
    }
    
    public void remove(T value) {
        if(head == null)
            return;
        if(head.checkValue(value)) {
            removeFromHead();
            return;
        }
        ListItem prev = head,
                it = head.next;
        while(it != null) {
            if(it.checkValue(value)) {
                if(it == tail)
                    removeFromTail();
                else
                    prev.next = it.next;
                return;
            }
            prev = it;
            it = it.next;
        }
    }

    public Iterable<T> after(T val){
        ListItem<T> v = findItem(val);

        if ((v==null)||(v.next==null)){
            start = null;
        } else {
            start = v.next;
        }
        return this::iterator;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private ListItem<T> current = start;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (current == null){
                    throw new NoSuchElementException();
                }
                T res = current.value;
                current = current.next;
                return res;
            }
        };
    }

    private static class ListItem<V> {
        V value;
        ListItem<V> next;

        ListItem(V value) {
            this.value = value;
        }
        
        boolean checkValue(V value) {
            return value == null && this.value == null
                    || value != null && value.equals(this.value);
        }
    }
}
