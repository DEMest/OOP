package ru.nsu.smolin;

/**
 * Main class to test heap.
 */
public class Main {
    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        int[] arr = {2, 3, 1, 4, 5};
        int cap = arr.length;
        MaxHeap a = new MaxHeap(arr.length);
        int [] result = a.sortArr(arr);
        for (int i = 0; i < cap; i++) {
            System.out.print(a.getElem(i) + " ");
        }
    }
}
