package model;

import java.io.Serializable;

public class Book implements Serializable {
    private String bookCode;
    private String isbn;
    private String name;
    private String authors;
    private int publishYear;

    public Book(String bookCode, String isbn, String name, String authors, int publishYear) {
        this.setBookCode(bookCode);
        this.setIsbn(isbn);
        this.setName(name);
        this.setAuthors(authors);
        this.setPublishYear(publishYear);
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        if (publishYear < 1454){
            throw new IllegalArgumentException("Publsih year should be greater than 1454");
        }
        this.publishYear = publishYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn.isEmpty()||isbn==null){
            throw new IllegalArgumentException("ISBN should be not-null & not-empty");
        }
        this.isbn = isbn;
    }
}
