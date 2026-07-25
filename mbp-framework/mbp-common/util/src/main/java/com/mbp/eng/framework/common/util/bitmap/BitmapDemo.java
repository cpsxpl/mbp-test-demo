package com.mbp.eng.framework.common.util.bitmap;

import org.roaringbitmap.IntConsumer;
import org.roaringbitmap.RoaringBitmap;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BitmapDemo {
    public void test1() {
        // 向 roaringBitmap1 中添加1、2、3、1000四个数字
        RoaringBitmap roaringBitmap1 = RoaringBitmap.bitmapOf(1, 2, 3, 1000);
        // 返回第3个数字是1000
        System.out.println(roaringBitmap1.select(3));

        roaringBitmap1.add(5);

        // 返回10000的索引,是4
        System.out.println(roaringBitmap1.rank(1000));
        System.out.println(roaringBitmap1.rank(3));
        System.out.println(roaringBitmap1.rank(2));
        System.out.println(roaringBitmap1.rank(1));

        // 是否包含1000和7,true和false
        System.out.println(roaringBitmap1.contains(1000));
        System.out.println(roaringBitmap1.contains(7));

        RoaringBitmap roaringBitmap2 = new RoaringBitmap();
        // 向 roaringBitmap2 添加10000-12000共2000个数字
        roaringBitmap2.add(10000L, 12000L);

        // 将两个 roaringBitmap1,roaringBitmap2 进行合并,数值进行合并,合并产生新的RoaringBitmap
        RoaringBitmap roaringBitmap3 = RoaringBitmap.or(roaringBitmap1, roaringBitmap2);

        // roaringBitmap1 和 roaringBitmap2 进行位运算,并将结果赋值给 roaringBitmap1
        roaringBitmap1.or(roaringBitmap2);

        // 判断 roaringBitmap1 与 roaringBitmap3 是否相等,true
        System.out.println(roaringBitmap1.equals(roaringBitmap3));

        // 查看roaringBitmap1中存储了多少个值,2004
        System.out.println(roaringBitmap1.getLongCardinality());

        // 两种遍历方式
        for (int i : roaringBitmap1) {
            System.out.println(i);
        }

        roaringBitmap1.forEach((Consumer<? super Integer>) i -> System.out.println(i.intValue()));
    }

    public void test2() {
        RoaringBitmap roaringBitmap = new RoaringBitmap();
        roaringBitmap.add(1L, 10L);

        // 遍历输出
        roaringBitmap.forEach((IntConsumer) i -> System.out.println(i));

        // 遍历放入List中
        List<Integer> numbers = new ArrayList<>();
        roaringBitmap.forEach((IntConsumer) numbers::add);
        System.out.println(numbers);

        roaringBitmap.runOptimize();

        int size = roaringBitmap.serializedSizeInBytes();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        roaringBitmap.serialize(buffer);
        // 将RoaringBitmap的数据转成字节数组,这样就可以直接存入数据库了,数据库字段类型BLOB
        byte[] bitmapData = buffer.array();
    }
}
