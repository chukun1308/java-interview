package com.chukun.interview.tools;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * CyclicBarrier 的基本使用
 */
public class CyclicBarrierOperator {

    public static void main(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(5,()->{
            System.out.println("人到齐了，开会");
        });

        for(int i=0;i<5;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" 到了 ");
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },String.format("%s%s","t",i)).start();
        }
    }
}
