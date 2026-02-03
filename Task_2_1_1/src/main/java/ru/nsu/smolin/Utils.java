package ru.nsu.smolin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.FileWriter;
import java.io.IOException;

public class Utils {

    private static final List<String> CSV_DATA = new ArrayList<>();
    private static FileWriter csvWriter;

    static {
        try {
            csvWriter = new FileWriter("chart_data.csv", false);
            csvWriter.write("Method,100k,500k,1M\n");
            CSV_DATA.add("Method,100k,500k,1M");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] generateLargePrimes(int maxLimit) {
        boolean[] isPrime = new boolean[maxLimit + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;

        for (int i = 2; i * i <= maxLimit; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= maxLimit; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= maxLimit; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }

        return primes.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void time(String name, Runnable func) {
        double start = (double) System.nanoTime() / 1000000;
        func.run();
        double end = ((double) System.nanoTime() / 1000000 - start);
        System.out.println(name + ": " + end + "ms");
    }

    public static void timeCSV(int size, String name, Runnable func) {
        double start = (double) System.nanoTime() / 1000000;
        func.run();
        double end = ((double) System.nanoTime() / 1000000 - start);
        System.out.println(size + " " + name + ": " + end + "ms");
        try {
            csvWriter.append(name + "(" + size + ")" + ": " + (double)end + "\n");
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeCSV() throws IOException {
        csvWriter.close();
        System.out.printf("csv writer closed\n");
    }
}
