package repositories.iterators;

import models.Book;

import java.util.Iterator;

public class BooksIterator implements Iterator<Book> {
    private final Book[] iterableStore;
    private int iterationPointer;

    public BooksIterator(Book[] bookStore) {
        if (bookStore == null) {
            throw new IllegalArgumentException("Reference to storage array must be not null");
        }
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
