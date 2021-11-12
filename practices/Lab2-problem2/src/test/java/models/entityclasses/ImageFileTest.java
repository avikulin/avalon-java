package models.entityclasses;

import models.dto.Dimensions;
import models.enums.FileFormats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageFileTest {
    ImageFile i;
    @BeforeEach
    void setup(){
        i = new ImageFile("1", FileFormats.PNG, 2, new Dimensions(10, 20));
    }

    @Test
    @DisplayName("Image file. Getting dimensions")
    void getImageDimensions() {
        assertEquals(i.getImageDimensions(), new Dimensions(10,20));
    }

    @Test
    @DisplayName("Image file. Setting dimensions")
    void setImageDimensions() {
        assertThrows(IllegalArgumentException.class, ()->i.setImageDimensions(null));
        i.setImageDimensions(new Dimensions(1,2));
        assertEquals(i.getImageDimensions(), new Dimensions(1,2));
    }

    @Test
    @DisplayName("Image file. Checking equality")
    void testEquals() {
        assertEquals(i, new ImageFile("1", FileFormats.PNG, 2,
                     new Dimensions(10, 20)));
        assertNotEquals(i, new ImageFile("2", FileFormats.PNG, 2,
                new Dimensions(10, 20)));
        assertNotEquals(i, new ImageFile("1", FileFormats.JPG, 2,
                new Dimensions(10, 20)));
        assertNotEquals(i, new ImageFile("1", FileFormats.PNG, 3,
                new Dimensions(10, 20)));
        assertNotEquals(i, new ImageFile("2", FileFormats.PNG, 2,
                new Dimensions(11, 20)));
        assertNotEquals(i, new ImageFile("2", FileFormats.PNG, 2,
                new Dimensions(10, 21)));
    }

    @Test
    @DisplayName("Image file. Testing detailed info")
    void getDetailedInfo() {
        assertEquals(i.getDetailedInfo(),"image (*.png), 10x20");
    }
}