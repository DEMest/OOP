package ru.nsu.smolin;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SampleTest {
    private static int[] check(int[] arr) {
        MaxHeap a = new MaxHeap(arr.length);
        return a.sortArr(arr);
    }

    private void checkSort(int[] arr) {
        int length = arr.length;
        int[] expected = Arrays.copyOf(arr, length);
        Arrays.sort(expected);
        int[] mysort = check(arr);
        assertArrayEquals(expected, mysort);
    }

    @Test
    void testArray() {
        checkSort(new int[]{1, 2, 3, 4, 5});
    }

    @Test
    void testRandomArray() {
        checkSort(new int[]{4, 3, 5, 1, 6, 3, 7});
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
