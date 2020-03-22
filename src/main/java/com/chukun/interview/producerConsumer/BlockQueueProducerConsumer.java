package com.chukun.interview.producerConsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用阻塞队列，实现生产者，消费者
 */
public class BlockQueueProducerConsumer {

    public static void main(String[] args) {

        ShareResource shareResource = new ShareResource(new ArrayBlockingQueue<>(10));
        //生产者线程
        new Thread(()->{
            shareResource.produce();
        },"AAA").start();

        //消费者线程
        new Thread(()->{
            shareResource.consumer();
        },"BBB").start();
    }

}


class ShareResource{
    //默认开启，生产者消费者模式
    private volatile boolean flag = true;

    private AtomicInteger atomicInteger = new AtomicInteger();

    private BlockingQueue<String> blockingQueue;

    public  ShareResource(BlockingQueue<String> blockingQueue){
        this.blockingQueue = blockingQueue;
    }

    /**
     * 生产者方法
     */
    public void produce(){
        String data = null;
        boolean retValue;
        while (flag){
            try {
                data = atomicInteger.incrementAndGet() + "";
                retValue = blockingQueue.offer(data,3, TimeUnit.SECONDS);
                if(retValue){
                    System.out.println(Thread.currentThread().getName()+"\t 插入队列成功 --->"+data);
                }else{
                    System.out.println(Thread.currentThread().getName()+"\t 插入队列失败 --->"+data);
                }
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName()+"\t 线程停止了");
    }

    /**
     * 生产者方法
     */
    public void consumer(){
        String result = null;
        while (flag){
            try {
                result = blockingQueue.poll(3,TimeUnit.SECONDS);
                if(result==null || "".equalsIgnoreCase(result)){
                    //消费者停止
                    flag = false;
                    System.out.println(Thread.currentThread().getName()+"\t 超过3秒，没获取到数据 --->");
                    System.out.println();
                    System.out.println();
                    return;
                }else{
                    System.out.println(Thread.currentThread().getName()+"\t 获取到数据 ---> "+result);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
