package com.chukun.interview.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chukun
 * aqs 深度解析
 */
public class AQSAnalyse {



    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        new Thread(() -> {
            try{
                lock.lock();
                System.out.println(Thread.currentThread().getName()+"---> come in....");
                TimeUnit.MINUTES.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        },"A").start();

        new Thread(() -> {
            try{
                lock.lock();
                System.out.println(Thread.currentThread().getName()+"---> come in....");
            } finally {
                lock.unlock();
            }

        },"B").start();

        new Thread(() -> {
            try{
                lock.lock();
                System.out.println(Thread.currentThread().getName()+"---> come in....");
            } finally {
                lock.unlock();
            }

        },"C").start();

    }

}
