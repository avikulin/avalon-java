package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    Book b;

    @BeforeEach
    void setup(){
        b = new Book("N", new Publisher("p", "l"), 2021, new String[]{"a"});
    }

    @Test
    void ConstructorTests(){
        assertThrows(IllegalArgumentException.class, ()-> new Book(null, new Publisher("p", "l"), 2021));
        assertThrows(IllegalArgumentException.class, ()-> new Book("", new Publisher("p", "l"), 2021));
        assertThrows(NullPointerException.class, ()-> new Book("Author", null, 2021));
        assertThrows(NullPointerException.class, ()-> new Book("Author", null, 0));
        assertThrows(IllegalArgumentException.class, ()-> new Book("N", new Publisher("p", "l"), 2021, null));
        assertThrows(IllegalArgumentException.class, ()-> new Book("N", new Publisher("p", "l"), "", 2021));
        Book b2 = new Book("N", new Publisher("p", "l"), 2021, new String[]{});
    }

    @Test
    void getName() {
        assertEquals(b.getName(), "N");
    }

    @Test
    void setName() {
        assertEquals(b.getName(), "N");
        b.setName("N1");
        assertEquals(b.getName(), "N1");
    }

    @Test
    void getPublisher() {
        assertEquals(b.getPublisher(), new Publisher("p", "l"));
    }

    @Test
    void setPublisher() {
        assertEquals(b.getPublisher(), new Publisher("p", "l"));
        b.setPublisher(new Publisher("p1", "l1"));
        assertEquals(b.getPublisher(), new Publisher("p1", "l1"));
    }

    @Test
    void getYearPublished() {
        assertEquals(b.getYearPublished(), 2021);
    }

    @Test
    void setYearPublished() {
        assertEquals(b.getYearPublished(), 2021);
        b.setYearPublished(1900);
        assertEquals(b.getYearPublished(), 1900);
    }

    @Test
    void getAuthors() {
        Book b2 = new Book("N", new Publisher("p", "l"), 2021, new String[]{"a1", "a2"});
    }

    @Test
    void setAuthors() {
        assertThrows(IllegalArgumentException.class, ()->b.setAuthors(new String[]{"a1", ""}));
        assertThrows(IllegalArgumentException.class, ()->b.setAuthors(new String[]{"a1", null}));
        assertThrows(IllegalArgumentException.class, ()->b.setAuthors(new String[]{"", "a2"}));
        assertThrows(IllegalArgumentException.class, ()->b.setAuthors(new String[]{null, "a2"}));
        assertThrows(IllegalArgumentException.class, ()->b.setAuthors(new String[]{"", ""}));
        assertThrows(IllegalArgumentException.class, ()->b.setAuthors(new String[]{null, null}));
        assertThrows(IllegalArgumentException.class, ()->b.setAuthors(new String[]{""}));

        b.setAuthors(new String[]{"a1","a2","a3"});
        assertTrue(Arrays.equals(b.getAuthors(), new String[]{"a1","a2","a3"}));
    }

    @Test
    void getNumberOfAuthors() {
        assertEquals(b.getNumberOfAuthors(), 1);

        Book b2 = new Book("N", new Publisher("p", "l"), 2021);
        assertEquals(b2.getNumberOfAuthors(), 0);

        Book b3 = new Book("N", new Publisher("p", "l"), 2021,
                            new String[]{"a1", "a2", "a3"});
        assertEquals(b3.getNumberOfAuthors(), 3);
    }

    @Test
    void getAuthorByIdx() {
        assertEquals(b.getAuthorByIdx(0), "a");
        assertThrows(IllegalArgumentException.class, ()->b.getAuthorByIdx(1));
        assertThrows(IllegalArgumentException.class, ()->b.getAuthorByIdx(-1));

        Book b2 = new Book("N", new Publisher("p", "l"), 2021);
        assertThrows(IllegalStateException.class, ()->b2.getAuthorByIdx(0));
    }

    @Test
    void getAuthorsStr() {
        assertEquals(new Book("N", new Publisher("p", "l"),2021).getAuthorsStr(), "");

        assertEquals(b.getAuthorsStr(), "a");

        assertEquals(new Book("N", new Publisher("p", "l"),
                2021, new String[]{"a1", "a2"}).getAuthorsStr(), "a1, a2");
        assertEquals(new Book("N", new Publisher("p", "l"),
                2021, new String[]{"a1", "a2", "a3"}).getAuthorsStr(), "a1, a2, a3");
    }

    @Test
    void testToString() {
        assertEquals(b.toString(), "a; N; p, l; 2021");

        b.setAuthors(new String[]{"a1","a2"});
        assertEquals(b.toString(), "a1, a2; N; p, l; 2021");

        b.setAuthors(new String[]{"a1","a2","a3"});
        assertEquals(b.toString(), "a1, a2, a3; N; p, l; 2021");
    }
}