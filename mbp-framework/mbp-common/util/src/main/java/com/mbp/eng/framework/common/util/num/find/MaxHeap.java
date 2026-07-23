package com.mbp.eng.framework.common.util.num.find;

/**
 * 大顶堆
 */
public class MaxHeap {
    int[] heap;
    int heapsize;

    public MaxHeap(int[] array) {
        this.heap = array;
        this.heapsize = heap.length;
    }

    public void BuildMaxHeap() {
        for (int i = heapsize / 2 - 1; i >= 0; i--) {
            // 依次向上将当前子树最大堆化
            Maxify(i);
        }
    }

    public void HeapSort() {
        for (int i = 0; i < heap.length; i++) {
            // 执行n次,将每个当前最大的值放到堆末尾
            swap(heap, 0, heapsize - 1);
            heapsize--;
            Maxify(0);
        }
    }

    public void Maxify(int i) {
        int l = 2 * i + 1;
        int r = 2 * i + 2;
        int largest;

        if (l < heapsize && heap[l] > heap[i])
            largest = l;
        else
            largest = i;
        if (r < heapsize && heap[r] > heap[largest])
            largest = r;
        // 如果largest等于i说明i是最大元素
        if (largest == i || largest >= heapsize)
            // largest超出heap范围说明不存在比i节点大的子女
            return;
        swap(heap, i, largest);
        Maxify(largest);
    }

    private void swap(int[] heap, int i, int largest) {
        // 交换i与largest对应的元素位置,在largest位置递归调用maxify
        int tmp = heap[i];
        heap[i] = heap[largest];
        heap[largest] = tmp;
    }

    public void IncreaseValue(int i, int val) {
        heap[i] = val;
        if (i >= heapsize || i <= 0 || heap[i] >= val)
            return;
        int p = Parent(i);
        if (heap[p] >= val)
            return;
        heap[i] = heap[p];
        IncreaseValue(p, val);
    }

    private int Parent(int i) {
        return (i - 1) / 2;
    }
}
