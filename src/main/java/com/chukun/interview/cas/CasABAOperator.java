package com.chukun.interview.cas;

import java.util.concurrent.TimeUnit;
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
 *     这种自旋的方式，解决不了ABA问题
 *
 */
public class CasABAOperator {

    public static void main(String[] args) {

        //这个案例，解决不了ABA问题，AtomicInteger只能对int字段需要，对对象修改，需要使用原子引用
        AtomicInteger atomicInteger = new AtomicInteger(5);
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+" the start value is : "+atomicInteger.get());
            System.out.println(atomicInteger.compareAndSet(5,10) +" the atomicInteger value is : "+atomicInteger.get());
            System.out.println(atomicInteger.compareAndSet(10,5) +" the atomicInteger value is : "+atomicInteger.get());
            System.out.println(Thread.currentThread().getName()+" the end value is : "+atomicInteger.get());
        },"t1").start();

        new Thread(()->{
            //睡眠三秒，保证t1线程已经设置完成
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" the start value is : "+atomicInteger.get());
            System.out.println(atomicInteger.compareAndSet(5,10) +" the atomicInteger value is : "+atomicInteger.get());
            System.out.println(Thread.currentThread().getName()+" the end value is : "+atomicInteger.get());
        },"t2").start();


    }
}
