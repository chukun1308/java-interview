package com.chukun.interview.oom;

import java.nio.ByteBuffer;

/**
 * 直接内存溢出
 *  Exception in thread "main" java.lang.OutOfMemoryError: Direct buffer memory
 *  java 分为堆内存，直接内存 ，直接内存本地分配，不需要用户态，到核心态的copy，速度快，
 *  但是gc不受jvm的控制
 */
public class DirectMemoryErrorOperator {

    public static void main(String[] args) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10 * 1024 * 1024);
        }catch (Exception e){
            e.printStackTrace();
            throw  e;
        }

    }
}
