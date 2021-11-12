package repositories;

import models.Publisher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublisherDictRepoTest {

    @Test
    void getItemByIdx() {
        PublisherDictRepo pdr = new PublisherDictRepo();
        assertEquals(pdr.getItemByIdx(0),new Publisher("Проспект", "Москва"));
        assertThrows(IndexOutOfBoundsException.class, ()->pdr.getItemByIdx(-1));
        assertThrows(IndexOutOfBoundsException.class, ()->pdr.getItemByIdx(4));
    }
}