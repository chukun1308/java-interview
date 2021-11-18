package com.chukun.interview.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用interrupt()方法，也可以唤醒被LockSupport.park()的线程
 * @author chukun
 */
public class LockSupportDemo02 {

    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() +"\t" + "进入....");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() +"\t" + "被唤醒了....");

        }, "A");
        a.start();

        a.interrupt();

        System.out.println("main over..........");

    }
}
