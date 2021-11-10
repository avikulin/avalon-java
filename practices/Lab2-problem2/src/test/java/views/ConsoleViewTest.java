package views;

import models.repository.FileRepository;
import models.templateclasses.AbstractFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ConsoleViewTest {
    ConsoleView cv;

    @Test
    @DisplayName("Console view. Testing the constructor")
    void Constructor() {
        assertThrows(IllegalArgumentException.class,
                ()->new ConsoleView(1,2,3));
        assertThrows(IllegalArgumentException.class,
                ()->new ConsoleView(1,20,30));
        assertThrows(IllegalArgumentException.class,
                ()->new ConsoleView(10,20,3));
        assertThrows(IllegalArgumentException.class,
                ()->new ConsoleView(11,9,30));
        assertThrows(IllegalArgumentException.class,
                ()->new ConsoleView(11,11,3));
        assertThrows(IllegalArgumentException.class,
                ()->new ConsoleView(50,50,20));
        cv = new ConsoleView(20,20,30);
    }

    @Test
    @DisplayName("Console view. Setting source")
    void registerSource() {
        cv = new ConsoleView(20,20,30);
        assertThrows(IllegalArgumentException.class, ()->cv.registerSource(null));
        FileRepository r = new FileRepository(new AbstractFile[10]);
        cv.registerSource(r);
    }
}