package repositories;

import models.Book;
import models.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooksRepoTest {
    BooksRepo br;
    PublisherDictRepo pdr;

    @BeforeEach
    void setup(){
        br = new BooksRepo(4);
        pdr = new PublisherDictRepo();
    }

    @Test
    void ConstructorTest(){
        assertThrows(IllegalArgumentException.class, ()-> new BooksRepo(0));
        assertThrows(IllegalArgumentException.class, ()-> new BooksRepo(-1));
        BooksRepo br2 = new BooksRepo(1);
    }

    @Test
    void addBookAndGetItemByIdx() {
        assertThrows(IllegalArgumentException.class, ()->br.addBook(null));

        for (int i=0; i<4;i++) {
            br.addBook(new Book("N", pdr.getItemByIdx(i), 2021));
            assertEquals(br.getItemByIdx(i), new Book("N", pdr.getItemByIdx(i), 2021));
        }

        assertThrows(RuntimeException.class,
                     ()->br.addBook(new Book("N", pdr.getItemByIdx(1), 2021)));
    }

    @Test
    void getFillState() {
        for (int i=0; i<4;i++) {
            br.addBook(new Book("N", pdr.getItemByIdx(i), 2021));
            assertEquals(br.getFillState(), i + 1);
        }
    }

    @Test
    void getSize() {
        assertEquals(br.getSize(), 4);
    }

    @Test
    void iterator() {
        int counter = 0;
        for (Book b: br){
            counter++;
        }
        assertEquals(counter, 0);

        br.addBook(new Book("N", pdr.getItemByIdx(0), 2021));
        for (Book b: br){
            counter++;
        }
        assertEquals(counter, 1);

        for (int i=0; i<3;i++) {
            br.addBook(new Book("N", pdr.getItemByIdx(i), 2021));
        }

        counter = 0;
        for (Book b: br){
            counter++;
        }
        assertEquals(counter, 4);

    }

    @Test
    void forEach() {
        int[] counter = new int[1];
        counter[0] = 0;

        for (int i=0; i<4;i++) {
            br.addBook(new Book("N", pdr.getItemByIdx(i), 2021));
        }

        br.forEach(b->counter[0]++);

        assertEquals(counter[0], 4);
    }
}