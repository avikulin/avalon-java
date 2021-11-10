package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {
    @Test
    void ConstructorTest(){
        assertThrows(IllegalArgumentException.class, ()->new Repository(0));
        assertThrows(IllegalArgumentException.class, ()->new Repository(-1));
        assertThrows(IllegalArgumentException.class, ()->new Repository(null));
        assertThrows(IllegalArgumentException.class, ()->new Repository(new boolean[]{}));
        Repository r1 = new Repository(1);
        Repository r2 = new Repository(new boolean[1]);
    }

    @Test
    void checkElement() {
        Repository r = new Repository(10);

        boolean[] b = new boolean[]{true,false,false,true};
        Repository r2 = new Repository(b);

        int i=0;
        for(boolean v:r2){
            assertEquals(v, b[i]);
            assertEquals(r2.checkElement(i), b[i]);

            assertEquals(r.checkElement(i), false);
            i++;
        }
    }

    @Test
    void setElement() {
        Repository r = new Repository(10);
        assertThrows(IllegalArgumentException.class, ()->r.setElement(-1));
        assertThrows(IllegalArgumentException.class, ()->r.setElement(11));
        assertEquals(r.checkElement(0), false);
        r.setElement(0);
        assertEquals(r.checkElement(0), true);

        assertEquals(r.checkElement(1), false);
        r.setElement(1);
        assertEquals(r.checkElement(1), true);

        assertEquals(r.checkElement(9), false);
        r.setElement(9);
        assertEquals(r.checkElement(9), true);
    }

    @Test
    void put() {
        Repository r = new Repository(10);
        assertThrows(IllegalArgumentException.class, ()->r.put(-1, true));
        assertThrows(IllegalArgumentException.class, ()->r.put(11, true));

        assertEquals(r.checkElement(0), false);
        r.put(0, true);
        assertEquals(r.checkElement(0), true);

        assertEquals(r.checkElement(1), false);
        r.put(1, true);
        assertEquals(r.checkElement(1), true);

        assertEquals(r.checkElement(9), false);
        r.put(9, true);
        assertEquals(r.checkElement(9), true);
    }

    @Test
    void unsetElement() {
        Repository r = new Repository(new boolean[]{true,true,true,true,true,true,true,true,true,true});

        assertThrows(IllegalArgumentException.class, ()->r.setElement(-1));
        assertThrows(IllegalArgumentException.class, ()->r.setElement(11));

        assertEquals(r.checkElement(0), true);
        r.unsetElement(0);
        assertEquals(r.checkElement(0), false);

        assertEquals(r.checkElement(1), true);
        r.unsetElement(1);
        assertEquals(r.checkElement(1), false);

        assertEquals(r.checkElement(9), true);
        r.unsetElement(9);
        assertEquals(r.checkElement(9), false);
    }

    @Test
    void invertElement() {
        Repository r = new Repository(10);
        assertThrows(IllegalArgumentException.class, ()->r.put(-1, true));
        assertThrows(IllegalArgumentException.class, ()->r.put(11, true));

        assertEquals(r.checkElement(0), false);
        r.put(0, true);
        assertEquals(r.checkElement(0), true);

        assertEquals(r.checkElement(1), false);
        r.put(1, true);
        assertEquals(r.checkElement(1), true);

        assertEquals(r.checkElement(9), false);
        r.put(9, true);
        assertEquals(r.checkElement(9), true);
    }

    @Test
    void countTrueElements() {
        assertEquals(new Repository(new boolean[]{false}).countTrueElements(),0);
        assertEquals(new Repository(new boolean[]{false, false}).countTrueElements(),0);
        assertEquals(new Repository(new boolean[]{true}).countTrueElements(),1);
        assertEquals(new Repository(new boolean[]{true, true}).countTrueElements(),2);
        assertEquals(new Repository(new boolean[]{true, false, false, true}).countTrueElements(),2);
        assertEquals(new Repository(new boolean[]{false, true, true, false}).countTrueElements(),2);
    }

    @Test
    void testToString() {
        assertEquals(new Repository(new boolean[]{false}).toString(),"[0]");
        assertEquals(new Repository(new boolean[]{false, false}).toString(),"[0, 0]");
        assertEquals(new Repository(new boolean[]{true}).toString(),"[1]");
        assertEquals(new Repository(new boolean[]{true, true}).toString(),"[1, 1]");
        assertEquals(new Repository(new boolean[]{true, false, false, true}).toString(),"[1, 0, 0, 1]");
        assertEquals(new Repository(new boolean[]{false, true, true, false}).toString(),"[0, 1, 1, 0]");
    }

    @Test
    void iterator() {
        Repository r = new Repository(1);
        int counter = 0;
        for(boolean b: r){
            counter++;
        }
        assertEquals(counter, 1);

        Repository r2 = new Repository(10);
        counter = 0;
        for(boolean b: r2){
            counter++;
        }
        assertEquals(counter, 10);
    }

    @Test
    void forEach() {
        final int[] counter = new int[1];
        Consumer<Boolean> f = (x) -> counter[0] = counter[0] + 1;
        Repository r = new Repository(1);
        r.forEach(f);
        assertEquals(counter[0], 1);

        Repository r2 = new Repository(10);
        counter[0] = 0;
        r2.forEach(f);
        assertEquals(counter[0], 10);
    }
}