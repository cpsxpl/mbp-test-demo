package com.mbp.eng.framework.common.util.bitmap;

import org.roaringbitmap.RoaringBitmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BitmapUtil {
    private static final Logger logger = LoggerFactory.getLogger(BitmapUtil.class);

    /**
     * bitmap转换为字节数组
     *
     * @param roaringBitmap
     * @return
     */
    public static byte[] bitMapToByteArray(RoaringBitmap roaringBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        byte[] bytes = new byte[0];
        try {
            roaringBitmap.serialize(dataOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 字节数组转换为bitmap
     *
     * @param bytes
     * @return
     */
    public static RoaringBitmap byteArrayToBitMap(byte[] bytes) {
        RoaringBitmap roaringBitmap = new RoaringBitmap();
        try {
            roaringBitmap.deserialize(ByteBuffer.wrap(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return roaringBitmap;
    }
}
