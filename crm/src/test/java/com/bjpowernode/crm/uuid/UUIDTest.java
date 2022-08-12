package com.bjpowernode.crm.uuid;

import org.junit.Test;

import java.util.UUID;

/**
 * @Author 临渊
 * @Date 2022-08-10 23:51
 */
public class UUIDTest {

    public static void main(String[] args) {
        // UUID uuid = UUID.randomUUID();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(uuid);
    }
}
