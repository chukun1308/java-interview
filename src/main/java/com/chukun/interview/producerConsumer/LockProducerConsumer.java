package com.chukun.interview.producerConsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用lock锁，实现生产者，消费者
 */
public class LockProducerConsumer {

    public static void main(String[] args) {

        ShareData sd = new ShareData();
        //生产者线程
        new Thread(()->{
            sd.produce();
        },"AAA").start();

        //消费者线程
        new Thread(()->{
            sd.consumer();
        },"BBB").start();
    }

}


class ShareData{
    private int number = 0;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    /**
     * 生产者方法
     */
    public void produce(){
        lock.lock();
        try{
            while(number!=0){
                condition.await();
            }
            number++;
            System.out.println(Thread.currentThread().getName()+"\t number:  "+number);
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 生产者方法
     */
    public void consumer(){
        lock.lock();
        try{
            while(number==0){
                condition.await();
            }
            number--;
            System.out.println(Thread.currentThread().getName()+"\t number:  "+number);
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
