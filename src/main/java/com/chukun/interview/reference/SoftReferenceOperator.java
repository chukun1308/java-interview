package com.chukun.interview.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * @author chukun
 *  软引用的基本操作
 */
public class SoftReferenceOperator {

    public static void main(String[] args) {
        //强引用，垃圾不会被回收
        Object o1 = new Object();
        /**
         * 软引用，内存不足，触发gc会被回收
         */
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        SoftReference<Object> softReference = new SoftReference<>(o1,referenceQueue);
        System.out.println("强引用: "+ o1);
        System.out.println("软引用: "+ softReference.get());
        System.out.println("软引用队列: "+ referenceQueue.poll());

        o1 = null;
        //手动gc
        System.gc();
        try{
           byte[] bytes = new byte[20*1024*1024];
        }catch (Exception e){

        }finally {
            System.out.println("内存不足--->强引用: "+ o1); // null
            System.out.println("内存不足--->软引用: "+ softReference.get()); // null
            System.out.println("内存不足--->软引用队列: "+ referenceQueue.poll()); //java.lang.ref.SoftReference@677327b6
        }
    }
}
