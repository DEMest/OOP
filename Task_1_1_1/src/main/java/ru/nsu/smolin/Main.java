package ru.nsu.smolin;

/**
 * Configure Maxheap & heapsort
 */
class MaxHeap {
    public int[] arr;
    private int hsize;

    /**
     * Initializes a new MaxHeap
     * @param n capacity of new heap
     */
    public MaxHeap(int n) {
        arr = new int[n];
        hsize = 0;
    }

    /**
     * function for swapping elements in MaxHeap
     * @param arr A heap of elements that we want to swap
     * @param a First Element
     * @param b Second Element
     */
    private void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    /**
     * Determines the parent element index of the
     * selected element index
     * @param a Selected element index
     * @return parent elemnt index
     */
    private int parentidx(int a) {
        return (a - 1) / 2;
    }

    /**
     * Returns the right child element index
     * @param a Root element index
     * @return child element index
     */
    private int right(int a) {
        return 2 * a + 2;
    }

    /**
     * Returns the left child element index
     * @param a Root element index
     * @return child element index
     */
    private int left(int a) {
        return 2 * a + 1;
    }

    /**
     * Implements the function to insert
     * an element into a heap
     * @param a element you want to insert
     */
    public void insert(int a) {
        int i = hsize;
        arr[i] = a;
        hsize++;

        while (i!=0 && arr[i] > arr[parentidx(i)]) {
            swap(arr, i, parentidx(i));
            i = parentidx(i);
        }
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
     * @param a Root node
     */
    private void heapify(int a) {
        int l = left(a);
        int r = right(a);
        int max = a;
        if(l < hsize && arr[l] > arr[max]) {
            max = l;
        }
        if(r < hsize && arr[r] > arr[max]) {
            max = r;
        }
        if(max != a) {
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

    /**
     * getting element from private param of heap
     * @param i index of getting element
     * @return element staying on index
     */
    public int getElem(int i) {
        return arr[i];
    }
}

/**
 * Main class to test heap
 */
public class Main {
    public static void main(String[] args) {
        int[] arr = {2,3,1,4,5};
        int cap = arr.length;
        MaxHeap a = new MaxHeap(cap);
        for (int j : arr) {
            a.insert(j);
        }
        a.heapsort();

        for (int i = 0; i < cap; i++) {
            System.out.print(a.arr[i] + " ");
        }
    }

    /**
     * checking array for tests
     * @param arr unsorted array
     * @return sorted
     */
    public static int[] check(int[] arr) {
        int cap = arr.length;
        MaxHeap a = new MaxHeap(cap);
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
