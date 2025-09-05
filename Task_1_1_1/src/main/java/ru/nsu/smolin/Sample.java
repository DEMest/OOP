package ru.nsu.smolin;

class MaxHeap {
    public int[] arr;
    private int hsize;

    public MaxHeap(int n) {
        arr = new int[n];
        hsize = 0;
    }

    private void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    private int parentidx(int a) {
        return (a - 1) / 2;
    }

    private int right(int a) {
        return 2 * a + 2;
    }

    private int left(int a) {
        return 2 * a + 1;
    }

    public void insert(int a) {
        int i = hsize;
        arr[i] = a;
        hsize++;

        while (i!=0 && arr[i] > arr[parentidx(i)]) {
            swap(arr, i, parentidx(i));
            i = parentidx(i);
        }
    }

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

    public void heapsort() {
        int savesize = hsize;

        for (int i = hsize - 1; i > 0; i--) {
            swap(arr, 0, i);
            hsize--;
            heapify(0);
        }
        hsize = savesize;
    }

    public int getElem(int i) {
        return arr[i];
    }
}

public class Sample {
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
