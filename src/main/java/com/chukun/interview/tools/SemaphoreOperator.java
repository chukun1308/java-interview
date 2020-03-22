package com.chukun.interview.tools;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author chukun
 * 信号量的基本使用，可以控制多个共享资源，
 * 当资源为一份时，就相当于互斥锁
 */
public class SemaphoreOperator {

    public static void main(String[] args) {
        //对多个共享资源的控制
        Semaphore semaphore = new Semaphore(3);
        for(int i=0;i<6;i++){
            new Thread(()->{
                try{
                    semaphore.acquire();
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName()+" \t come in parking");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
