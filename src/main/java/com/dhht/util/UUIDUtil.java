package com.dhht.util;

import java.util.UUID;

public class UUIDUtil {
    /**
     * 获得一个无分隔符UUID
     *
     * @return String UUID
     */
    public static String generate() {
        String str = UUID.randomUUID().toString();
        return str.replaceAll("-", "");
    }
}