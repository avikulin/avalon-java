package repositories.iterators;

import models.Book;

import java.util.Iterator;

public class BooksIterator implements Iterator<Book> {
    private final Book[] iterableStore;
    private int fillFactor;
    private int iterationPointer;

    public BooksIterator(Book[] bookStore, int fillFactor) {
        if (bookStore == null) {
            throw new IllegalArgumentException("Reference to storage array must be not null");
        }
        if (fillFactor < 0) {
            throw new IllegalArgumentException("Fill factor must be positive value");
        }
        this.iterableStore = bookStore;
        this.iterationPointer = 0;
        this.fillFactor = fillFactor;
    }

    @Override
    public boolean hasNext() {
        return iterationPointer < fillFactor;
    }

    @Override
    public Book next() {
        Book res = iterableStore[iterationPointer];
        iterationPointer++;
        return res;
    }
}
