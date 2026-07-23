package com.mbp.eng.framework.common.util.array;

import java.util.Arrays;

public class ArrayText {
    public static void main(String[] args) throws Exception {
        int[] arrays = {2, 5, -2, 6, -3, 8, 0, -7, -9, 4};
        Arrays.sort(arrays);
        printArray("数组排序结果为：", arrays);
        int index = Arrays.binarySearch(arrays, 1);
        System.out.println("元素1所在的位置(负数为不存在)：" + index);
        int newIndex = -index - 1;
        arrays = insertElement(arrays, 1, newIndex);
        printArray("数组添加元素1", arrays);
    }

    private static void printArray(String message, int[] arrays) {
        System.out.println(message + ":[length:" + arrays.length + "]");
        for (int i = 0; i < arrays.length; i++) {
            if (i != 0) {
                System.out.print(",");
            }
            System.out.print(arrays[i]);
        }
        System.out.println();
    }

    private static int[] insertElement(int[] original, int element, int index) {
        int length = original.length;
        int[] destination = new int[length + 1];
        System.arraycopy(original, 0, destination, 0, index);
        destination[index] = element;
        System.arraycopy(original, index, destination, index + 1, length - index);
        return destination;
    }
}