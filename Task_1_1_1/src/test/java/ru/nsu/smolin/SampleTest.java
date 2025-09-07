package ru.nsu.smolin;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SampleTest {
    private void checkSort(int[] arr) {
        int length = arr.length;
        int[] expected = Arrays.copyOf(arr, length);
        Arrays.sort(expected);
        int[] mysort = Main.check(arr);
        assertArrayEquals(expected, mysort);
    }

    @Test
    void testArray() {
        checkSort(new int[]{1,2,3,4,5});
    }

    @Test
    void testRandomArray() {
        checkSort(new int[]{4,3,5,1,6,3,7});
    }

    @Test
    void testOneElementArray() {
        checkSort(new int[]{42});
    }

    @Test
    void testEmptyArray() {
        checkSort(new int[]{});
    }

    @Test
    void checkMain() {
        Main.main(new String[] {});
        assertTrue(true);
    }
}