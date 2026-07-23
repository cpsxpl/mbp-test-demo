package com.mbp.eng.framework.common.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class IDGenerator {
    private static final HashFunction function = Hashing.murmur3_32();

    /**
     * @param pinId
     * @param timestamp
     * @return
     */
    public static Long generateId(String pinId, Long timestamp) {
        StringBuilder builder = new StringBuilder();
        HashCode hashcode = function.hashString(pinId, Charset.forName("utf-8"));
        builder.append(timestamp).append(Math.abs(hashcode.asInt()));
        if (builder.length() > 16) {
            return Long.valueOf(builder.substring(0, 16));
        } else {
            return Long.valueOf(builder.toString());
        }
    }
}
