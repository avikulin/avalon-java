package models.entityclasses;

import models.dto.Dimensions;
import models.dto.Duration;
import models.enums.FileFormats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoFileTest {
    VideoFile v;

    @BeforeEach
    void setup(){
        v = new VideoFile("1", FileFormats.AVI, 2, "abc",
                new Duration(3665), new Dimensions(10,20));
    }

    @Test
    @DisplayName("Video file. Getting thumbnail pic dimensions")
    void getThumbPicDimensions() {
        assertEquals(v.getThumbPicDimensions(), new Dimensions(10,20));
    }

    @Test
    @DisplayName("Video file. Setting thumbnail pic dimensions")
    void setThumbPicDimensions() {
        assertThrows(IllegalArgumentException.class, ()->v.setThumbPicDimensions(null));
        v.setThumbPicDimensions(new Dimensions(100, 200));
        assertEquals(v.getThumbPicDimensions(), new Dimensions(100, 200));
    }

    @Test
    @DisplayName("Video file. Checking equality")
    void testEquals() {
        assertEquals(v, new VideoFile("1", FileFormats.AVI, 2, "abc",
                new Duration(3665), new Dimensions(10,20)));
        assertNotEquals(v, new VideoFile("2", FileFormats.AVI, 2, "abc",
                new Duration(3665), new Dimensions(10,20)));
        assertNotEquals(v, new VideoFile("1", FileFormats.DOCX, 2, "abc",
                new Duration(3665), new Dimensions(10,20)));
        assertNotEquals(v, new VideoFile("1", FileFormats.AVI, 3, "abc",
                new Duration(3665), new Dimensions(10,20)));
        assertNotEquals(v, new VideoFile("1", FileFormats.AVI, 2, "abc1",
                new Duration(3665), new Dimensions(10,20)));
        assertNotEquals(v, new VideoFile("1", FileFormats.AVI, 2, "abc",
                new Duration(3660), new Dimensions(10,20)));
        assertNotEquals(v, new VideoFile("1", FileFormats.AVI, 2, "abc",
                new Duration(3665), new Dimensions(11,20)));
        assertNotEquals(v, new VideoFile("1", FileFormats.AVI, 2, "abc",
                new Duration(3665), new Dimensions(10,21)));
    }

    @Test
    @DisplayName("Video file. Testing detailed info")
    void getDetailedInfo() {
        assertEquals(v.getDetailedInfo(), "video (*.avi), abc, 01:01:05, 10x20");
    }
}