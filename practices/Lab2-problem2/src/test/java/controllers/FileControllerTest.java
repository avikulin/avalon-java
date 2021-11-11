package controllers;

import interfaces.base.FileSystemObject;
import models.entityclasses.DocumentFile;
import models.enums.FileFormats;
import models.repository.FileRepository;
import models.templateclasses.AbstractFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import views.ConsoleView;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class FileControllerTest {
    FileController fc;
    @BeforeEach
    void setup(){
        fc = new FileController();
    }

    @Test
    @DisplayName("File controller. Check model reference is not null")
    void registerModel() {
        assertThrows(IllegalArgumentException.class, ()->fc.registerModel(null));
        FileRepository<AbstractFile> r = new FileRepository<>(new AbstractFile[10]);
        fc.registerModel(r);
    }

    @Test
    @DisplayName("File controller. Check view reference is not null")
    void registerView() {
        assertThrows(IllegalArgumentException.class, ()->fc.registerView(null));
        ConsoleView view = new ConsoleView(11,11,11);
        fc.registerView(view);
    }

    @Test
    @DisplayName("File controller. Working with data")
    void appendData() {
        FileRepository r = new FileRepository(new AbstractFile[10]);
        fc.registerModel(r);

        fc.appendData(new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));
        fc.appendData(new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));
        fc.appendData(new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));
        fc.appendData(new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));
        fc.appendData(new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));

        int counter = 0;
        Iterator<FileSystemObject> it = fc.iterateDataItems();
        while(it.hasNext()){
            FileSystemObject f = it.next();
            counter++;
            assertEquals(f,new DocumentFile("j110-lab2-hiers.doc", FileFormats.DOCX, 23212, 2));
        }
        assertEquals(counter, 5);
    }
}