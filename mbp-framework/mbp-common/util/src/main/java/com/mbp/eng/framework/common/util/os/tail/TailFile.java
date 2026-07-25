package com.mbp.eng.framework.common.util.os.tail;

import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TailFile {
    private static final long step = 5000;

    public static final String ARR = "arr";
    public static final String POINT = "point";

    public static Map tail(Long end, long num, File file, String charset) throws Exception {
        if (num <= 0 || (end != null && end < 0)) {
            throw new IllegalArgumentException();
        }
        Map map = new HashMap();
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long tempEnd = (end == null ? randomAccessFile.length() : end);
            System.out.println("tempEnd=====" + tempEnd);
            long myPoint = tempEnd > step ? (tempEnd - step) : 0;
            System.out.println("myPoint=====" + myPoint);

            randomAccessFile.seek(myPoint);
            LinkedList<Object[]> linkedListQueue = new LinkedList<Object[]>();
            String temp;
            int n = 0;
            while ((temp = randomAccessFile.readLine()) != null) {
                if (++n == 1 && myPoint != 0) {
                    System.out.println(temp);
                    continue;
                }
                Object[] objects = new Object[2];
                long point = randomAccessFile.getFilePointer();
                if (point >= tempEnd && end != null) {
                    break;
                }
                objects[0] = point;
                objects[1] = new String(temp.getBytes("8859_1"), charset);
                if (linkedListQueue.size() == num) {
                    linkedListQueue.poll();
                }
                linkedListQueue.offer(objects);
            }

            if (linkedListQueue.size() < num && myPoint > 0) {
                long lastNum = num - linkedListQueue.size();
                Object[] header = linkedListQueue.peek();
                if (header == null) {
                    throw new RuntimeException("FileUtil step:" + step + " not enough long");
                }
                Map m = tail((Long) header[0], lastNum, file, charset);
                map.put(POINT, m.get(POINT));
                map.put(ARR, ArrayUtils.addAll((Object[]) m.get(ARR), linkedListQueue.toArray()));
            } else if (linkedListQueue.size() > 0) { // 获取的行数不够,并且没有到达TOP
                map.put(POINT, linkedListQueue.peek()[0]);
                map.put(ARR, linkedListQueue.toArray());
            }
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
}
