package com.chukun.interview.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @author chukun
 *  弱引用的基本操作
 */
public class WeakReferenceOperator {

    public static void main(String[] args) {
        //强引用，垃圾不会被回收
        Object o1 = new Object();
        /**
         * 弱引用，内存不足，会被回收
         */
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        WeakReference<Object> weakReference = new WeakReference<>(o1,referenceQueue);
        System.out.println("强引用: "+ o1);
        System.out.println("弱引用: "+ weakReference.get());
        System.out.println("弱引用队列: "+ referenceQueue.poll());

        o1 = null;
        try{
           byte[] bytes = new byte[20*1024*1024];
        }catch (Exception e){

        }finally {
            System.out.println("内存不足--->强引用: "+ o1); // null
            System.out.println("内存不足--->弱引用: "+ weakReference.get()); // null
            System.out.println("内存不足--->弱引用队列: "+ referenceQueue.poll()); //java.lang.ref.SoftReference@677327b6
        }
    }
}
