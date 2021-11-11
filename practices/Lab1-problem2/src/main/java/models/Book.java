package models;

import java.util.StringJoiner;

public class Book {
    private String name;
    private Publisher publisher;
    private int yearPublished;
    private String[] authors;

    public Book(String name, Publisher publisher, int yearPublished) {
        this.name = name;
        this.publisher = publisher;
        this.yearPublished = yearPublished;
    }

    public Book(String name, Publisher publisher, String author, int yearPublished) {
        this(name, publisher, yearPublished);
        this.authors = new String[]{author};
    }

    public Book(String name, Publisher publisher, int yearPublished, String[] authors) {
        this(name, publisher, yearPublished);
        setAuthors(authors);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if ((name == null) || name.isEmpty()) {
            throw new IllegalArgumentException("Name must be set to non-blank value");
        }
        this.name = name;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        if (publisher == null) {
            throw new NullPointerException("Publisher must be set");
        }
        this.publisher = publisher;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        if (yearPublished <= 0) {
            throw new IllegalArgumentException("Year must be positive value above 0");
        }
        this.yearPublished = yearPublished;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        if (authors == null) {
            throw new IllegalArgumentException("Authors must be not null");
        }

        for (String author : authors) {
            if ((author == null) || (author.isEmpty())) {
                throw new IllegalArgumentException("Authors must not contain blank strings of nulls");
            }
        }

        this.authors = authors;
    }

    public void print() {
        System.out.println(this);
    }

    public int getNumberOfAuthors() {
        return (authors != null) ? authors.length : 0;
    }

    public String getAuthorByIdx(int idx) {
        if (authors.length == 0) {
            return "";
        }

        if ((idx < 0) || (idx >= authors.length)) {
            throw new IllegalArgumentException("Index must not be less 0, and must not exceed the array length");
        }
        return authors[idx];
    }

    public String getAuthorsStr() {
        if ((authors == null) || (authors.length == 0)) {
            return "";
        }

        if (authors.length == 1) {
            return authors[0];
        }
        StringJoiner res = new StringJoiner(", ");
        for (String author : authors) {
            res.add(author);
        }
        return res.toString();
    }

    public static void printAll(Book[] books) {
        for (Book book : books) {
            book.print();
        }
        System.out.println("\n");
    }

    @Override
    public String toString() {
        StringJoiner res = new StringJoiner("; ");
        if (getNumberOfAuthors() > 0) {
            res.add(getAuthorsStr());
        }
        res.add(name);
        res.add(publisher.toString());
        res.add(Integer.toString(yearPublished));

        return res.toString();
    }
}
