package repo;

import model.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BooksList {
    private List<Book> books;
    private Set<String> codes;
    private static final String FILE_NAME = "books.dat";

    public BooksList() throws IOException, ClassNotFoundException {
        books = new ArrayList<>();
        codes = new HashSet<String>();

        File f = new File(FILE_NAME);

        if (f.exists()){

            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))){
                books = (List<Book>) ois.readObject();
                for (Book b:books){
                    codes.add(b.getBookCode());
                }
            }
        }
    }

    public void save() throws IOException {
        File f = new File(FILE_NAME);
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))){
            oos.writeObject(books);
        }
    }

    public Book get(int index){
        return books.get(index);
    }

    public int getCount(){
        return books.size();
    }

    public void add(Book book){
        if (codes.contains(book.getIsbn())){
            throw new IllegalArgumentException("Duplicate book");
        }
        books.add(book);
        codes.add(book.getIsbn());
    }

    public void remove(int index){
        books.remove(index);
        books.remove(books.get(index).getIsbn());
    }


}
