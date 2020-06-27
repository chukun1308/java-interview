package com.chukun.interview.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author chukun
 *  通过设置线程停止标记，停止线程
 */
public class ProducerConsumerInterrupter {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer>  storage = new ArrayBlockingQueue<>(8);

        StopProducer  stopProducer = new StopProducer(storage);
        Thread stopProducerThread = new Thread(stopProducer);
        stopProducerThread.start();

        TimeUnit.MILLISECONDS.sleep(500);


        Consumer consumer = new Consumer(storage);
        while (consumer.needMoreNums()) {
            System.out.println(consumer.storage.take() + "被消费了");
            TimeUnit.MILLISECONDS.sleep(100);
        }
        System.out.println("消费者不需要更多的数据了");
        // interrupt 语句来中断
        stopProducerThread.interrupt();
    }
}

/**
 * 声明了一个生产者 Producer，通过 volatile 标记的初始值为 false 的布尔值 canceled 来停止线程
 */
class StopProducer implements Runnable{

    private BlockingQueue<Integer> storage;

    public StopProducer(BlockingQueue<Integer> storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        int num = 0;
        try {
            // while 的判断语句是 num 是否小于 100000 及 canceled 是否被标记
            while (num < 10000 && !Thread.currentThread().isInterrupted()) {
                /**
                 * while 循环体中判断 num 如果是 50 的倍数就放到 storage 仓库中，
                 * storage 是生产者与消费者之间进行通信的存储器，当 num 大于 100000 或被通知停止时，
                 * 会跳出 while 循环并执行 finally 语句块
                 */
                if (num % 50 == 0) {
                    storage.put(num);
                    System.out.println(num + " 是50的倍数，被放在仓库了");

                }
                num++;

            }
        }catch (InterruptedException e) {
            // 再次设置线程中断标记
            Thread.currentThread().interrupt();
        }finally {
            System.out.println("生产者结束运行....");
        }
    }
}
