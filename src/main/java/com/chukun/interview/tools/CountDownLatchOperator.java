package com.chukun.interview.tools;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @author chukun
 *  CountDownLatch的基本用法
 */
public class CountDownLatchOperator {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);

        for(int i=0;i<5;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" 离开了 ");
                latch.countDown();
            },String.format("%s%s","t",i)).start();
        }

        latch.await();
        System.out.println(Thread.currentThread().getName()+" 离开了 ");
    }
}
