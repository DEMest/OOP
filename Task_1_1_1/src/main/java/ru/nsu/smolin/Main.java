package ru.nsu.smolin;

/**
 * Main class to test heap.
 */
public class Main {
    /**
     * Application entry point.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        int[] arr = {2, 3, 1, 4, 5};
        int cap = arr.length;
        MaxHeap a = new MaxHeap();
        a.maxHeap(cap);
        for (int j : arr) {
            a.insert(j);
        }
        a.heapsort();

        for (int i = 0; i < cap; i++) {
            System.out.print(a.arr[i] + " ");
        }
    }

    /**
     * Checking array for tests.
     * @param arr unsorted array
     * @return sorted
     */
    public static int[] check(int[] arr) {
        int cap = arr.length;
        MaxHeap a = new MaxHeap();
        a.maxHeap(cap);
        for (int j : arr) {
            a.insert(j);
        }
        a.heapsort();
        for (int i = 0; i < cap; i++) {
            arr[i] = a.getElem(i);
        }
        return arr;
    }
}
