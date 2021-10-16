package ru.avalon.javapp.devj110.booksdemo.repositories;

import ru.avalon.javapp.devj110.booksdemo.models.Book;
import ru.avalon.javapp.devj110.booksdemo.repositories.iterators.BooksIterator;

import java.util.Iterator;
import java.util.function.Consumer;

public class BooksRepo implements Iterable<Book> {
    private Book[] booksStore;
    private int fillPointer;

    public BooksRepo(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be positive value");
        }
        booksStore = new Book[size];
        fillPointer = 0;
    }

    public void addBook(Book item) {
        if (item == null) {
            throw new IllegalArgumentException("Item reference must be set");
        }
        if (fillPointer == booksStore.length) {
            throw new RuntimeException("Max. storage size exceeded");
        }
        booksStore[fillPointer] = item;
        fillPointer++;
    }

    public int getFillState() {
        return fillPointer;
    }

    public int getSize() {
        return booksStore.length;
    }

    public Book getItemByIdx(int idx) {
        if ((idx < 0) || (idx >= booksStore.length)) {
            throw new IndexOutOfBoundsException("Illegal index passed");
        }
        return booksStore[idx];
    }

    @Override
    public Iterator<Book> iterator() {
        return new BooksIterator(booksStore);
    }

    @Override
    public void forEach(Consumer<? super Book> action) {
        if (action == null) {
            throw new IllegalArgumentException("Action func reference must be set");
        }
        for (Book b : this) {
            action.accept(b);
        }
    }
}
