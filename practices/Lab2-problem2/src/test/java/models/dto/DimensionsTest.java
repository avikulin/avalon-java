package models.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DimensionsTest {
    Dimensions d;
    @BeforeEach
    void setup() {
        d = new Dimensions(0, 0);
    }

    @Test
    @DisplayName("Dimensions. Negative width")
    void setWidth() {
        Throwable ex = assertThrows(IllegalArgumentException.class, ()->new Dimensions(-1,10));
    }

    @Test
    @DisplayName("Dimensions. Negative height")
    void setHeight() {
        Throwable ex = assertThrows(IllegalArgumentException.class, ()->new Dimensions(10,-1));
    }

    @Test
    @DisplayName("Dimension. Get width")
    void getWidth() {
        assertEquals(new Dimensions(100,0).getWidth(), 100,()->"Error in internal state (height)");
    }

    @Test
    @DisplayName("Dimension. Get height")
    void getHeight() {
        assertEquals(new Dimensions(0,100).getHeight(), 100,()->"Error in internal state (height)");
    }

    @Test
    @DisplayName("Dimension. Serialization")
    void testToString() {
        assertEquals(new Dimensions(0, 999999).toString(),"0x999999");
    }

    @Test
    @DisplayName("Dimension. Equals")
    void testEquals() {
        assertEquals(new Dimensions(10,20), new Dimensions(10,20));
        assertNotEquals(new Dimensions(10,20), new Dimensions(11,20));
        assertNotEquals(new Dimensions(11,21), new Dimensions(11,20));
    }
}