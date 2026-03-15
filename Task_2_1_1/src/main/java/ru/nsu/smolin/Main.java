package ru.nsu.smolin;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static ru.nsu.smolin.Utils.closeCSV;
import static ru.nsu.smolin.Utils.generateLargePrimes;
import static ru.nsu.smolin.Utils.time;
import static ru.nsu.smolin.Utils.timeCSV;

public class Main {
    public static void main(String[] args) throws IOException {
        int[] sizes = {5000, 100_000, 500_000, 1_000_000};
        for(int j : sizes) {
            for (int i = 0; i < 2000; i++) {
                int[] arr = generateLargePrimes(j);
                timeCSV(j, "Sequentional", i, () -> Main.sequentialPrime(arr));
            }
            for (int i = 0; i < 2000; i++) {
                int[] arr = generateLargePrimes(j);
                timeCSV(j, "Thread_2", i, () -> Main.threadedPrime(arr, 2));
            }
            for (int i = 0; i < 2000; i++) {
                int[] arr = generateLargePrimes(j);
                timeCSV(j, "Thread_8", i, () -> Main.threadedPrime(arr, 8));
            }
            for (int i = 0; i < 2000; i++) {
                int[] arr = generateLargePrimes(j);
                timeCSV(j, "Thread_32", i, () -> Main.threadedPrime(arr, 32));
            }
            for (int i = 0; i < 2000; i++) {
                int[] arr = generateLargePrimes(j);
                timeCSV(j, "parallelStream", i, () -> Main.parallelPrime(arr));
            }
        }
        closeCSV();

    }

    public static boolean isPrime(int n) {
        if (n <= 3) {
            return n >= 2;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNonPrime(int n) {
        return !isPrime(n);
    }

    public static boolean sequentialPrime(int[] numbers) {
        for (int num : numbers) {
            if (!isPrime(num)) {
                return true;
            }
        }
        return false;
    }

    public static boolean parallelPrime(int[] numbers) {
        List<Integer> nums = IntStream.of(numbers).boxed().toList();
        return nums.parallelStream().anyMatch(Main::isNonPrime);
    }

    static volatile boolean foundNonPrime;
    public static boolean threadedPrime(int[] numbers, int threads) {
        int chunkSize = (numbers.length + threads - 1) / threads;
        Thread[] threads1 = new Thread[threads];
        foundNonPrime = false;
        for(int i = 0; i < threads; i++) {
            final int startIdx = i * chunkSize;
            final int endIdx = Math.min(startIdx + chunkSize, numbers.length);

            threads1[i] = new Thread(() -> {
                for (int j = startIdx; j < endIdx; j++) {
                    if (foundNonPrime) {
                        return;
                    }
                    if (!isPrime(numbers[j])) {
                        foundNonPrime = true;
                        return;
                    }
                }
            });
            threads1[i].start();
        }

        try {
            for (Thread thread : threads1) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return foundNonPrime;
    }
}
