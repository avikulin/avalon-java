package ru.avalon.javapp.devj110.booksdemo.repositories.iterators;

import ru.avalon.javapp.devj110.booksdemo.models.Book;

import java.util.Iterator;

public class BooksIterator implements Iterator<Book> {
    private Book[] iterableStore;
    private int iterationPointer;

    public BooksIterator(Book[] bookStore){
        iterableStore = bookStore;
        iterationPointer = 0;
    }

    @Override
    public boolean hasNext() {
        return iterationPointer < iterableStore.length;
    }

    @Override
    public Book next() {
        Book res = iterableStore[iterationPointer];
        iterationPointer++;
        return res;
    }
}
