package repository;

import interfaces.BooleanRepository;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {
    @Test
    void ConstructorTest(){
        assertThrows(IllegalArgumentException.class, ()->new Repository(0));
        assertThrows(IllegalArgumentException.class, ()->new Repository(-1));
        assertThrows(IllegalArgumentException.class, () -> new Repository(null));
        assertThrows(IllegalArgumentException.class, () -> new Repository(new int[]{}));
        Repository r1 = new Repository(1);
        Repository r2 = new Repository(new int[1]);
    }

    @Test
    void checkElement() {
        int[] b = new int[]{9};
        Repository r = new Repository(b);
        Repository r2 = new Repository(4);

        for (int i = 0; i < 4; i++) {
            assertEquals(r.checkElement(i), (b[0] & 1 << i)!=0);
            assertFalse(r2.checkElement(i));
            i++;
        }
    }

    @Test
    void setElement() {
        Repository r = new Repository(10);
        assertThrows(IllegalArgumentException.class, ()->r.setElement(-1));
        assertThrows(IllegalArgumentException.class, ()->r.setElement(11));

        assertFalse(r.checkElement(0));
        r.setElement(0);
        assertTrue(r.checkElement(0));

        assertFalse(r.checkElement(1));
        r.setElement(1);
        assertTrue(r.checkElement(1));

        assertFalse(r.checkElement(9));
        r.setElement(9);
        assertTrue(r.checkElement(9));
    }

    @Test
    void put() {
        Repository r = new Repository(10);
        assertThrows(IllegalArgumentException.class, ()->r.put(-1, true));
        assertThrows(IllegalArgumentException.class, ()->r.put(11, true));

        assertFalse(r.checkElement(0));
        r.put(0, true);
        assertTrue(r.checkElement(0));

        assertFalse(r.checkElement(1));
        r.put(1, true);
        assertTrue(r.checkElement(1));

        assertFalse(r.checkElement(9));
        r.put(9, true);
        assertTrue(r.checkElement(9));
    }

    @Test
    void unsetElement() {
        int[] b = new int[]{0b1000000000000000000000000000001, 0b1000000000000000000000000000001};
        Repository r = new Repository(b);

        assertThrows(IllegalArgumentException.class, ()->r.setElement(-1));
        assertThrows(IllegalArgumentException.class, ()->r.setElement(62));

        assertTrue(r.checkElement(0));
        r.unsetElement(0);
        assertFalse(r.checkElement(0));

        assertTrue(r.checkElement(30));
        r.unsetElement(30);
        assertFalse(r.checkElement(30));

        assertTrue(r.checkElement(31));
        r.unsetElement(31);
        assertFalse(r.checkElement(31));

        assertTrue(r.checkElement(61));
        r.unsetElement(61);
        assertFalse(r.checkElement(61));

        assertEquals(b[0], 0);
        assertEquals(b[1], 0);
    }

    @Test
    void invertElement() {
        int[] b = new int[]{0b1000000000000000000000000000001, 0b1000000000000000000000000000001};
        Repository r = new Repository(b);

        assertThrows(IllegalArgumentException.class, ()->r.setElement(-1));
        assertThrows(IllegalArgumentException.class, ()->r.setElement(62));

        assertTrue(r.checkElement(0));
        r.invertElement(0);
        assertFalse(r.checkElement(0));

        assertTrue(r.checkElement(30));
        r.invertElement(30);
        assertFalse(r.checkElement(30));

        assertTrue(r.checkElement(31));
        r.invertElement(31);
        assertFalse(r.checkElement(31));

        assertTrue(r.checkElement(61));
        r.invertElement(61);
        assertFalse(r.checkElement(61));

        assertEquals(b[0], 0);
        assertEquals(b[1], 0);
    }

    @Test
    void countTrueElements() {
        assertEquals(new Repository(new int[]{0b0}).countTrueElements(),0);
        assertEquals(new Repository(new int[]{0b0, 0b0}).countTrueElements(),0);
        assertEquals(new Repository(new int[]{0b1000000000000000000000000000000}).countTrueElements(),1);
        assertEquals(new Repository(new int[]{0b1000000000000000000000000000000,
                                              0b0000000000000000000000000000001}).countTrueElements(),2);
        assertEquals(new Repository(new int[]{0b0000000000000000000000000000001,
                                              0b1000000000000000000000000000000}).countTrueElements(),2);
        assertEquals(new Repository(new int[]{0b1000000000000000000000000000001,
                                              0b1000000000000000000000000000001}).countTrueElements(),4);
    }

    @Test
    void testToString() {
        assertEquals(new Repository(1).toString(),"[0]");
        assertEquals(new Repository(2).toString(),"[0, 0]");

        BooleanRepository r = new Repository(1);
        r.setElement(0);
        assertEquals(r.toString(),"[1]");

        BooleanRepository r2 = new Repository(2);
        r2.setElement(0);
        r2.setElement(1);
        assertEquals(r2.toString(),"[1, 1]");

        assertEquals(new Repository(new int[]{0b1000000000000000000000000000001}).toString(),
               "[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1]");

        assertEquals(new Repository(new int[]{0b1000000000000000000000000000001,
                                              0b1000000000000000000000000000001}).toString(),
                "[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, " +
                       "1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1]");
    }

}