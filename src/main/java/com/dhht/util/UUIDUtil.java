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

    public static String generate10() {
        String str = UUID.randomUUID().toString().substring(0,9);
        return str.replaceAll("-", "");
    }
}