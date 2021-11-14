package dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitPositionTest {

    @Test
    void ConstructorTest() {
/*        BitPosition pb = new BitPosition(0);
        assertEquals(pb.getMajor(), 0);
        assertEquals(pb.getMinor(), 0);
        assertEquals(pb.getMask(), 0b1);

        BitPosition pb1 = new BitPosition(1);
        assertEquals(pb1.getMajor(), 0);
        assertEquals(pb1.getMinor(), 1);
        assertEquals(pb1.getMask(), 0b10);

        BitPosition pb2 = new BitPosition(2);
        assertEquals(pb2.getMajor(), 0);
        assertEquals(pb2.getMinor(), 2);
        assertEquals(pb2.getMask(), 0b100);

        BitPosition pb3 = new BitPosition(29);
        assertEquals(pb3.getMajor(), 0);
        assertEquals(pb3.getMinor(), 29);
        assertEquals(pb3.getMask(), 0b100000000000000000000000000000);*/

        {
            BitPosition pb4 = new BitPosition(30);
            assertEquals(pb4.getMajor(), 0);
            assertEquals(pb4.getMinor(), 30);
            assertEquals(pb4.getMask(), 0b1000000000000000000000000000000);
        }
        {
            BitPosition pb5 = new BitPosition(31);
            assertEquals(pb5.getMajor(), 1);
            assertEquals(pb5.getMinor(), 0);
            assertEquals(pb5.getMask(), 0b1);
        }
        {
            BitPosition pb6 = new BitPosition(60);
            assertEquals(pb6.getMajor(), 1);
            assertEquals(pb6.getMinor(), 29);
            assertEquals(pb6.getMask(), 0b100000000000000000000000000000);
        }
        {
            BitPosition pb7 = new BitPosition(61);
            assertEquals(pb7.getMajor(), 1);
            assertEquals(pb7.getMinor(), 30);
            assertEquals(pb7.getMask(), 0b1000000000000000000000000000000);
        }
    }
}