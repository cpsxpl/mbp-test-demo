package com.mbp.eng.framework.common.util.num.find;

public class SelectSort {
    // 简单选择排序
    public static void selectSort(int[] array, int[] subscript, int n) {
        int i;
        int j;
        int max;
        int tmp;
        for (i = 0; i < n; i++) {
            max = i;
            for (j = i; j < array.length - 1; j++) {
                if (array[max] < array[j + 1])
                    max = j + 1;
            }
            if (max != i) {
                tmp = array[i];
                array[i] = array[max];
                array[max] = tmp;

                tmp = subscript[i];
                subscript[i] = subscript[max];
                subscript[max] = tmp;
            }
        }
    }

    public static void main(String[] args) {
        int[] input = {1, 1, 2, 2, 3, 4, 5, 6, 7, 8, 9, 200, 12, 180, 190, 220, 221, 3, 5, 226};
        int[] subscript = new int[input.length];
        for (int i = 0; i < subscript.length; ++i)
            subscript[i] = i;
        int n = 6;
        selectSort(input, subscript, n);
        for (int i = 0; i < n; ++i)
            System.out.print(subscript[i] + "  ");
        System.out.println();
    }
}
