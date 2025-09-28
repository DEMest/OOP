package ru.nsu.smolin;

import java.util.Arrays;

/**
 * Configure Maxheap and heapsort.
 */
public class MaxHeap {
    private final int[] arr;
    private int hsize;

    /**
     * Initializes a new MaxHeap.
     *
     * @param n capacity of new heap
     */
    public MaxHeap(int n) {
        arr = new int[n];
        hsize = 0;
    }

    /**
     * Implements the function to insert
     * an element into a heap.
     *
     * @param a element you want to insert
     */
    public void insert(int a) {
        int i = hsize;
        arr[i] = a;
        hsize++;

        while (i != 0 && arr[i] > arr[parentidx(i)]) {
            swap(arr, i, parentidx(i));
            i = parentidx(i);
        }
    }

    /**
     * Build max-heap by elements of input array, and perform heapsort.
     * Inital array doesn't change.
     *
     * @param arri Input array
     * @return copy of original array
     */
    public int[] sortArr(int[] arri) {
        if (arri.length == 0) {
            return arri;
        }
        for (int i : arri) {
            insert(i);
        }
        heapsort();
        return Arrays.copyOf(arr, hsize);
    }

    /**
     * Getting element from private param of heap.
     *
     * @param i index of getting element
     * @return element staying on index
     */
    public int getElem(int i) {
        return arr[i];
    }

    /**
     * Function for swapping elements in MaxHeap.
     *
     * @param arr A heap of elements that we want to swap
     * @param a First Element
     * @param b Second Element
     */
    private void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    private int parentidx(int a) {
        return (a - 1) / 2;
    }

    private int right(int a) {
        return 2 * a + 2; }

    private int left(int a) {
        return 2 * a + 1;
    }

    /**
     * Ensures the maxHeap property
     * for a subtree.
     * It compares a node with its
     * children, swaps with the
     * larger child if necessary,
     * and recursive going down until
     * the subtree satisfies the heap
     * property.
     *
     * @param a Root node
     */
    private void heapify(int a) {
        int l = left(a);
        int r = right(a);
        int max = a;
        if (l < hsize && arr[l] > arr[max]) { max = l; }
        if (r < hsize && arr[r] > arr[max]) { max = r; }
        if (max != a) {
            swap(arr, a, max);
            heapify(max);
        }
    }

    /**
     * Sorts the elements of the heap in ascending order
     * swapping root element with last, deacreasing capacity of heap.
     * the heapify tree without root element to recursive determine
     * max element (root) in a tree.
     */
    public void heapsort() {
        int savesize = hsize;
        for (int i = hsize - 1; i > 0; i--) {
            swap(arr, 0, i);
            hsize--;
            heapify(0);
        }
        hsize = savesize;
    }
}