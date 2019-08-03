package io.trbl.blurhash;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Base83Test {

    @Test
    public void testSingleDigits() {
        for (int i = 0; i < 83; ++i) {
            String expected = new String(Base83.ALPHABET, i, 1);
            assertEquals(i + " encodes", expected, Base83.encode(i, 1));
        }
    }

    @Test
    public void test0000() {
        assertEquals("0000", Base83.encode(0, 4));
    }

    @Test
    public void test0001() {
        assertEquals("0001", Base83.encode(1, 4));
    }

    @Test
    public void test0010() {
        assertEquals("0010", Base83.encode(83, 4));
    }

    @Test
    public void test0011() {
        assertEquals("0011", Base83.encode(83 + 1, 4));
    }

    @Test
    public void test00X0() {
        assertEquals("00~0", Base83.encode(83 * 82, 4));
    }

    @Test
    public void test0100() {
        assertEquals("0100", Base83.encode(83 * 83, 4));
    }

    @Test
    public void test00XXEncode() {
        assertEquals("00~~", Base83.encode(83 * 82 + 82, 4));
    }

    @Test
    public void test0XXXDecode() {
        assertEquals(  82
                     + 82 * 83
                     + 82 * 83 * 83, Base83.decode("0~~~"));
    }
}
