package models.entityclasses;

import models.dto.Duration;
import models.enums.FileFormats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultimediaFileTest {
    MultimediaFile m;
    @BeforeEach
    void setup(){
        m = new MultimediaFile("1", FileFormats.MP3, 2, "abc", new Duration(3665));
    }

    @Test
    @DisplayName("Multimedia file. Getting content description")
    void getContentDescription() {
        assertEquals(m.getContentDescription(),"abc");
    }

    @Test
    @DisplayName("Multimedia file. Getting content description")
    void setContentDescription() {
        assertThrows(IllegalArgumentException.class, ()->m.setContentDescription(null));
        assertThrows(IllegalArgumentException.class, ()->m.setContentDescription(""));
        m.setContentDescription("bcd");
        assertEquals(m.getContentDescription(), "bcd");
    }

    @Test
    @DisplayName("Multimedia file. Getting duration")
    void getDuration() {
        assertEquals(m.getDuration(), new Duration(3665));
    }

    @Test
    @DisplayName("Multimedia file. Setting duration")
    void setDuration() {
        assertThrows(IllegalArgumentException.class, ()->m.setDuration(null));
        m.setDuration(new Duration(1));
        assertEquals(m.getDuration(),new Duration(1));
    }

    @Test
    @DisplayName("Multimedia file. Checking equality")
    void testEquals() {
        assertEquals(m, new MultimediaFile("1", FileFormats.MP3, 2,
                                    "abc", new Duration(3665)));
        assertNotEquals(m, new MultimediaFile("2", FileFormats.MP3, 2,
                "abc", new Duration(3665)));
        assertNotEquals(m, new MultimediaFile("1", FileFormats.ACC, 2,
                "abc", new Duration(3665)));
        assertNotEquals(m, new MultimediaFile("1", FileFormats.MP3, 3,
                "abc", new Duration(3665)));
        assertNotEquals(m, new MultimediaFile("1", FileFormats.MP3, 2,
                "abc1", new Duration(3665)));
        assertNotEquals(m, new MultimediaFile("1", FileFormats.MP3, 2,
                "abc", new Duration(3660)));
    }

    @Test
    @DisplayName("Multimedia file. Testing detailed info")
    void getDetailedInfo() {
        assertEquals(m.getDetailedInfo(), "audio (*.mp3), abc, 01:01:05");
    }
}