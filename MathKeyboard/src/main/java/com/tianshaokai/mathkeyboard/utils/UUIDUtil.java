package com.tianshaokai.mathkeyboard.utils;

import java.util.UUID;

public class UUIDUtil {
    public UUIDUtil() {
    }

    public static String generator() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String result = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        return result;
    }
}