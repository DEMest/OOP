package ru.nsu.smolin;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import static ru.nsu.smolin.Utils.generateLargePrimes;
import static ru.nsu.smolin.Utils.time;

public class Main {
    public static void main(String[] args) {

        int[] arr = generateLargePrimes(1_000_000);
//        int[] arr = {2, 37, 7919, 104651, 1000003};
        time("Sequantial", () -> sequentialPrime(arr));
        time("Threaded", () -> threadedPrime(arr, 32));
        time("ParallelStream", () -> parallelPrime(arr));

        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println("Потоков в parallelStream: " + commonPool.getParallelism());
        System.out.println("Ядер CPU: " + Runtime.getRuntime().availableProcessors());

        System.out.println(arr.length);
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
