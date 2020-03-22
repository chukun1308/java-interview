package com.chukun.interview.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chukun
 *  CAS的基本操作
 *
 *  CAS 源码分析: 主要使用了自旋锁，cas是cpu的一条原语，保证原子操作
 *
 *  private volatile int value;  volatile保证可见性，防止指令重排序
 *  public final boolean compareAndSet(int expect, int update) {
 *         return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
 *     }
 *
 *    public final int getAndSetInt(final Object o, final long n, final int n2) {
 *         int intVolatile;
 *         do {
 *             //先获取Object对象 偏移地址 n 的value 值
 *             intVolatile = this.getIntVolatile(o, n);
 *             //自旋方式compareAndSwap ,这样做的好处，不需要阻塞线程，但是也有缺点，对cpu的消耗大
 *         } while (!this.compareAndSwapInt(o, n, intVolatile, n2));
 *         return intVolatile;
 *     }
 *
 */
public class CasOperator {

    public static void main(String[] args) {

        AtomicInteger atomicInteger = new AtomicInteger(5);

        System.out.println(atomicInteger.compareAndSet(5,10) +" the atomicInteger value is : "+atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5,15) +" the atomicInteger value is : "+atomicInteger.get());
    }
}
