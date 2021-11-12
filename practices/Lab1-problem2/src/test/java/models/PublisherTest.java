package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublisherTest {
    @Test
    void ConstructorTest(){
        assertThrows(IllegalArgumentException.class, ()->new Publisher(null, "Loc"));
        assertThrows(IllegalArgumentException.class, ()->new Publisher("", "Loc"));
        assertThrows(IllegalArgumentException.class, ()->new Publisher("Pub", null));
        assertThrows(IllegalArgumentException.class, ()->new Publisher("Puc", ""));
        Publisher p = new Publisher("p", "l");
    }

    @Test
    void getName() {
        Publisher p = new Publisher("p", "l");
        assertEquals(p.getName(), "p");
    }

    @Test
    void setName() {
        Publisher p = new Publisher("p", "l");
        assertEquals(p.getName(), "p");
        p.setName("p1");
        assertEquals(p.getName(), "p1");
    }

    @Test
    void getLocation() {
        Publisher p = new Publisher("p", "l");
        assertEquals(p.getLocation(), "l");
    }

    @Test
    void setLocation() {
        Publisher p = new Publisher("p", "l");
        assertEquals(p.getLocation(), "l");
        p.setLocation("l1");
        assertEquals(p.getLocation(), "l1");
    }

    @Test
    void testToString() {
        Publisher p = new Publisher("p", "l");
        assertEquals(p.toString(), "p, l");
    }
}