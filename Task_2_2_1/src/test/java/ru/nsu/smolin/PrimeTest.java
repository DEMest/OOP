package ru.nsu.smolin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static ru.nsu.smolin.Utils.generateLargePrimes;
import static ru.nsu.smolin.Utils.closeCSV;
import static ru.nsu.smolin.Utils.timeCSV;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;


public class PrimeTest {
    private int[] smallPrimes;
    private int[] hasNonPrime;

    @BeforeEach
    void setUp() {
        smallPrimes = new int[]{2, 3, 5, 7, 11, 13, 17};
        hasNonPrime = new int[]{2, 4, 5, 7, 9};
    }

    @Test
    void testSequentialPrime() {
        assertFalse(Main.sequentialPrime(smallPrimes), "Все простые");
        assertTrue(Main.sequentialPrime(hasNonPrime), "Есть составные");
    }

    @Test
    void testThreadedPrime() {
        assertFalse(Main.threadedPrime(smallPrimes, 4));
        assertTrue(Main.threadedPrime(hasNonPrime, 4));
    }

    @Test
    void testParallelStreamPrime() {
        assertFalse(Main.parallelPrime(smallPrimes));
        assertTrue(Main.parallelPrime(hasNonPrime));
    }

    @Test
    void testPerformanceLarge() throws IOException {
        System.out.println("Performance test:");

        int[] sizes = {100_000, 500_000, 1_000_000};

        for (int size : sizes) {
            int[] arr = generateLargePrimes(size);

            timeCSV(size, "Sequential", () -> Main.sequentialPrime(arr));
            timeCSV(size, "Sequential", () -> Main.sequentialPrime(arr));
            timeCSV(size, "Threads_4", () -> Main.threadedPrime(arr, 4));
            timeCSV(size, "Threads_8", () -> Main.threadedPrime(arr, 8));
            timeCSV(size, "Threads_16", () -> Main.threadedPrime(arr, 16));
            timeCSV(size, "Threads_32", () -> Main.threadedPrime(arr, 32));
            timeCSV(size, "parallelStream", () -> Main.parallelPrime(arr));
        }
        closeCSV();
    }
}
