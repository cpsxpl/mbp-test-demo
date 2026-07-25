package com.mbp.eng.framework.common.util.num.find;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 从海量数据中查找出前k个最大值,精确时间复杂度为：K + (n - K) * lgk,空间复杂度为 O（k）,目前为所有算法中最优算法
 */
public class FindMinNumIncluedTopN {
    /**
     * 从海量数据中查找出前k个最大值
     *
     * @param k
     * @return
     * @throws IOException
     */
    public static int[] findMinNumIncluedTopN(String pathFile, int k) throws IOException {
        Long start = System.nanoTime();

        int[] array = new int[k];
        int index = 0;

        // 从文件导入海量数据
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(pathFile)));
        String str = null;

        // 先读出前n条数据,构建堆
        do {
            str = bufferedReader.readLine();
            if (str != null) {
                array[index] = Integer.parseInt(str);
            }
            index++;
        } while (str != null && index <= k - 1);

        // 初始化堆
        MinHeap minHeap = new MinHeap(array);
        for (int i : minHeap.heap) {
            System.out.print(i + " ");
        }

        // 构建小顶堆
        minHeap.BuildMinHeap();
        System.out.println();
        System.out.println("构建小顶堆之后:");
        for (int i : minHeap.heap) {
            System.out.print(i + " ");
        }
        System.out.println();
        // 遍历文件中剩余的n（文件数据容量,假设为无限大）-k条数据,如果读到的数据比heap[0]大,就替换之,同时更新堆
        while (str != null) {
            str = bufferedReader.readLine();
            if (str != null && !"".equals(str.trim())) {
                if (Integer.parseInt(str) > minHeap.heap[0]) {
                    minHeap.heap[0] = Integer.parseInt(str);
                    // 调整小顶堆
                    minHeap.Minify(0);
                }
            }
        }
        // 最后对堆进行排序(降序)
        minHeap.HeapSort();

        Long end = System.nanoTime();
        long time = end - start;
        System.out.println("用时：" + time + "纳秒");
        for (int i : minHeap.heap) {
            System.out.println(i);
        }
        return minHeap.heap;
    }

    public static void main(String[] args) throws IOException {
        String pathFile = "d:/number.txt";
        int[] maxNum = findMinNumIncluedTopN(pathFile, 3);
    }
}
