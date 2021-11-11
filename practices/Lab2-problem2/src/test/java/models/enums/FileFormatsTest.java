package models.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileFormatsTest {

    @Test
    @DisplayName("FileFormats. Serialization to string.")
    void testToString() {
        assertAll("Testing serialization",
                ()->assertEquals(FileFormats.AVI.toString(), "video (*.avi)"),
                ()->assertEquals(FileFormats.MP3.toString(), "audio (*.mp3)"),
                ()->assertEquals(FileFormats.DOCX.toString(), "ms word document (*.docx)"),
                ()->assertEquals(FileFormats.PNG.toString(), "image (*.png)"),
                ()->assertEquals(FileFormats.XLSX.toString(), "ms excel table (*.xlsx)")
        );
    }
}