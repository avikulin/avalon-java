package models.templateclasses;

import models.dto.Dimensions;
import models.dto.Duration;
import models.entityclasses.DocumentFile;
import models.entityclasses.ImageFile;
import models.entityclasses.MultimediaFile;
import models.entityclasses.VideoFile;
import models.enums.FileFormats;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbstractFileTest {
    AbstractFile f;

    @Test
    void setFileName() {
        f = new DocumentFile("1", FileFormats.DOCX, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> f.setFileName(null));
        assertThrows(IllegalArgumentException.class, () -> f.setFileName(""));

        f.setFileName("abc");
        assertEquals(f.getFileName(), "abc");
    }

    @Test
    @DisplayName("Abstract file. Documents. Check file format validation")
    void setFormatId1() {
        f = new DocumentFile("1", FileFormats.DOCX, 2, 3);
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(null));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.PNG));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.JPG));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.MP3));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.ACC));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.MP4));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.AVI));
        f.setFormatId(FileFormats.XLSX);
        assertEquals(f.getFormatId(), FileFormats.XLSX);
    }

    @Test
    @DisplayName("Abstract file. Images. Check file format validation")
    void setFormatId2() {
        f = new ImageFile("1", FileFormats.PNG, 2, new Dimensions(10,20));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(null));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.DOCX));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.XLSX));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.MP3));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.ACC));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.MP4));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.AVI));
        f.setFormatId(FileFormats.JPG);
        assertEquals(f.getFormatId(), FileFormats.JPG);
    }

    @Test
    @DisplayName("Abstract file. Multimedia. Check file format validation")
    void setFormatId3() {
        f = new MultimediaFile("1", FileFormats.MP3, 2, "abc", new Duration(1));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(null));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.PNG));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.JPG));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.DOCX));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.XLSX));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.AVI));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.MP4));

        f.setFormatId(FileFormats.ACC);
        assertEquals(f.getFormatId(), FileFormats.ACC);
    }

    @Test
    @DisplayName("Abstract file. Documents. Check file format validation")
    void setFormatId4() {
        f = new VideoFile("1", FileFormats.AVI, 2, "abc",
                new Duration(1), new Dimensions(10,20));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(null));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.PNG));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.JPG));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.MP3));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.DOCX));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.XLSX));
        assertThrows(IllegalArgumentException.class, ()->f.setFormatId(FileFormats.ACC));

        f.setFormatId(FileFormats.MP4);
        assertEquals(f.getFormatId(), FileFormats.MP4);
    }

    @Test
    void setSizeInBytes() {
        f = new DocumentFile("1", FileFormats.DOCX, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> f.setSizeInBytes(-1));
        f.setSizeInBytes(1000);
        assertEquals(f.getSizeInBytes(), 1000);
    }
}