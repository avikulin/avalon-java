package models.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DurationTest {
    @Test
    @DisplayName("Constructor test. Duration in seconds")
    void ConstructorTest1() {
        Duration d1 = new Duration(3665);
        assertAll("Construction by time in seconds",
                () -> assertEquals(d1.getHours(), 1),
                () -> assertEquals(d1.getMinutes(), 1),
                () -> assertEquals(d1.getSeconds(), 5));
    }

    @Test
    @DisplayName("Constructor test. Duration in minutes & seconds")
    void ConstructorTest2() {
        Duration d2 = new Duration(59, 59);
        assertAll("Construction by time in minutes & seconds",
                () -> assertEquals(d2.getHours(), 0),
                () -> assertEquals(d2.getMinutes(), 59),
                () -> assertEquals(d2.getSeconds(), 59));
    }

    @Test
    @DisplayName("Constructor test. Duration in hours & minutes & seconds")
    void ConstructorTest3() {
        Duration d1 = new Duration(10, 10, 10);
        assertAll("Construction by time in minutes & seconds",
                () -> assertEquals(d1.getHours(), 10),
                () -> assertEquals(d1.getMinutes(), 10),
                () -> assertEquals(d1.getSeconds(), 10));
    }

    @Test
    @DisplayName("Constructor test. Assert wrong seconds value")
    void ConstructorTest4() {
        assertAll("Construction with illegal params",
                () -> assertThrows(IllegalArgumentException.class, () -> new Duration(0, 0, 60)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Duration(0, 0, -1))
        );
    }

    @Test
    @DisplayName("Constructor test. Assert wrong minutes")
    void ConstructorTest5() {
        assertAll("Construction with illegal params",
                () -> assertThrows(IllegalArgumentException.class, () -> new Duration(0, 60, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Duration(0, -1, 0))
        );
    }

    @Test
    @DisplayName("Constructor test. Assert wrong hours")
    void ConstructorTest6() {
        assertThrows(IllegalArgumentException.class, () -> new Duration(-1, 0, 0));
    }

    @Test
    @DisplayName("To string. All segments")
    void testToString1() {
        assertEquals(new Duration(1, 1, 1).toString(), "01:01:01", () -> "Wrong serialization");
    }

    @Test
    @DisplayName("To string. Only seconds")
    void testToString2() {
        assertEquals(new Duration(1).toString(), "00:01", () -> "Wrong serialization");
    }

    @Test
    @DisplayName("To string. Minutes and seconds")
    void testToString3() {
        assertEquals(new Duration(1, 1).toString(), "01:01", () -> "Wrong serialization");
    }

    @Test
    @DisplayName("To string. With normalization")
    void testToString4() {
        assertEquals(new Duration(43330).toString(), "12:02:10", () -> "Wrong serialization or normalization");
    }

    @Test
    @DisplayName("Equals")
    void testEquals() {
        assertEquals(new Duration(1, 1, 1), new Duration(3661));
        assertNotEquals(new Duration(2, 1, 1), new Duration(3661));
        assertNotEquals(new Duration(1, 2, 1), new Duration(3661));
        assertNotEquals(new Duration(1, 1, 2), new Duration(3661));
    }
}