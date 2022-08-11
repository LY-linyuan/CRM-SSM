package com.bjpowernode.crm.commons.utils;

import java.util.UUID;

/**
 * @Author 临渊
 * @Date 2022-08-10 23:56
 */
public class UUIDUtils {

    /**
     *  获取UUID的值
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
