package models.repository;

import interfaces.base.FileSystemObject;
import models.dto.Duration;
import models.entityclasses.DocumentFile;
import models.entityclasses.MultimediaFile;
import models.entityclasses.VideoFile;
import models.enums.FileFormats;
import models.templateclasses.AbstractFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileRepositoryTest {

    @Test
    @DisplayName("File repository. Constructor test")
    void Constructor() {
        assertThrows(IllegalArgumentException.class,()->new FileRepository<>(null));
        assertThrows(IllegalArgumentException.class,()->new FileRepository<>(new FileSystemObject[]{}));
    }

    @Test
    @DisplayName("File repository. Put null")
    void put0() {
        FileRepository<AbstractFile> r = new FileRepository<>(new AbstractFile[10]);
        assertThrows(IllegalArgumentException.class,()->r.put(null));

        int counter = 0;
        for (AbstractFile f: r){
            counter++;
        }
        assertEquals(counter, 0);
    }

    @Test
    @DisplayName("File repository. Abstract file test")
    void put1() {
        FileRepository<AbstractFile> r = new FileRepository<>(new AbstractFile[10]);
        r.put(new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));
        r.put(new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));
        r.put(new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));
        r.put(new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));
        r.put(new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));

        int counter = 0;
        for (AbstractFile f: r){
            counter++;
            assertEquals(f,new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));
        }
        assertEquals(counter, 5);
    }

    @Test
    @DisplayName("File repository. Multimedia file test")
    void put2() {
        FileRepository<MultimediaFile> r = new FileRepository<>(new MultimediaFile[10]);
        r.put(new MultimediaFile("06-PrettyGirl.mp3", FileFormats.MP3, 7893454,
                "Eric Clapton, Pretty Girl", new Duration(5, 28)));
        r.put(new MultimediaFile("06-PrettyGirl.mp3", FileFormats.MP3, 7893454,
                "Eric Clapton, Pretty Girl", new Duration(5, 28)));
        r.put(new MultimediaFile("06-PrettyGirl.mp3", FileFormats.MP3, 7893454,
                "Eric Clapton, Pretty Girl", new Duration(5, 28)));
        r.put(new MultimediaFile("06-PrettyGirl.mp3", FileFormats.MP3, 7893454,
                "Eric Clapton, Pretty Girl", new Duration(5, 28)));
        r.put(new MultimediaFile("06-PrettyGirl.mp3", FileFormats.MP3, 7893454,
                "Eric Clapton, Pretty Girl", new Duration(5, 28)));

        int counter = 0;
        for (MultimediaFile f: r){
            counter++;
            assertEquals(f,new MultimediaFile("06-PrettyGirl.mp3", FileFormats.MP3, 7893454,
                    "Eric Clapton, Pretty Girl", new Duration(5, 28)));
        }
        assertEquals(counter, 5);
    }
}