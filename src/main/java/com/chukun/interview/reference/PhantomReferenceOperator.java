package com.chukun.interview.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 *  @author chukun
 *  虚引用的基本操作
 *  phantomReference.get()总为空，一般配合ReferenceQueue使用
 *  触发gc，会把对象放入引用列表中，，，做一些后置的处理
 */
public class PhantomReferenceOperator {

    public static void main(String[] args) {
        Object o1 = new Object();
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        PhantomReference<Object> phantomReference = new PhantomReference<>(o1,referenceQueue);

        o1 = null;
        System.gc();
        System.out.println("虚引用: "+ phantomReference.get());
        System.out.println("虚引用队列: "+ referenceQueue.poll());
    }
}
