package models.entityclasses;

import models.enums.FileFormats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentFileTest {
    DocumentFile d;

    @BeforeEach
    void setup() {
        d = new DocumentFile("1", FileFormats.DOCX, 2, 3);
    }

    @Test
    @DisplayName("DocumentFile. Set number of pages")
    void getNumberOfPages() {
        assertEquals(d.getNumberOfPages(), 3);
    }

    @Test
    @DisplayName("DocumentFile. Setting number of pages")
    void setNumberOfPages() {
        assertThrows(IllegalArgumentException.class, () -> d.setNumberOfPages(-1));
        assertThrows(IllegalArgumentException.class, () -> d.setNumberOfPages(0));
        d.setNumberOfPages(99);
        assertEquals(d.getNumberOfPages(), 99);
    }

    @Test
    @DisplayName("DocumentFile. Testing equality")
    void testEquals() {
        assertEquals(new DocumentFile("1", FileFormats.DOCX, 2, 3),
                new DocumentFile("1", FileFormats.DOCX, 2, 3));
        assertNotEquals(new DocumentFile("1", FileFormats.DOCX, 2, 3),
                new DocumentFile("2", FileFormats.DOCX, 2, 3));
        assertNotEquals(new DocumentFile("2", FileFormats.DOCX, 2, 3),
                new DocumentFile("2", FileFormats.XLSX, 2, 3));
        assertNotEquals(new DocumentFile("2", FileFormats.DOCX, 2, 3),
                new DocumentFile("2", FileFormats.DOCX, 3, 3));
        assertNotEquals(new DocumentFile("2", FileFormats.DOCX, 2, 3),
                new DocumentFile("2", FileFormats.DOCX, 2, 1));
    }

    @Test
    @DisplayName("DocumentFile. Testing detailed info")
    void getDetailedInfo() {
        assertEquals(d.getDetailedInfo(), "ms word document (*.docx), 3 pages");
    }
}