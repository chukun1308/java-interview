package com.chukun.interview.oom;

/**
 * 堆空间不足，创建大对象会报错
 * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 */
public class HeapSpaceErrorOperator {

    public static void main(String[] args) {

        byte[] bytes = new byte[10*1024*1024];
    }
}
