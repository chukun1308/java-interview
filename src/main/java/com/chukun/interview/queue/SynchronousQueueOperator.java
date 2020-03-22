package com.chukun.interview.queue;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author 不存储数据
 *  适用于生产者，消费者模型，生产一个，没有消费者去消费，就阻塞生产
 *  生产一个，消费一个
 */
public class SynchronousQueueOperator {

    public static void main(String[] args) {
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

        //生产者线程
        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName()+"\t put a");
                synchronousQueue.put("a");
                System.out.println(Thread.currentThread().getName()+"\t put b");
                synchronousQueue.put("b");
                System.out.println(Thread.currentThread().getName()+"\t put c");
                synchronousQueue.put("c");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "AAA").start();

        //消费者线程
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName()+"\t 消费了 "+synchronousQueue.poll());
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName()+"\t 消费了 "+synchronousQueue.poll());
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName()+"\t 消费了 "+synchronousQueue.poll());
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }, "BBB").start();
    }
}
