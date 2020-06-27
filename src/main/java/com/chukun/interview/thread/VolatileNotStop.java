package com.chukun.interview.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author chukun
 *  volatile 修饰标记位不适用的场景
 */

public class VolatileNotStop {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer>  storage = new ArrayBlockingQueue<>(8);
        Producer  producer = new Producer(storage);

        Thread producerThread = new Thread(producer);
        producerThread.start();

        TimeUnit.MILLISECONDS.sleep(500);

        Consumer consumer = new Consumer(storage);
        while (consumer.needMoreNums()) {
            System.out.println(consumer.storage.take() + "被消费了");
            TimeUnit.MILLISECONDS.sleep(100);
        }
        System.out.println("消费者不需要更多的数据了");
        //一旦消费不需要更多数据了，我们应该让生产者也停下来，但是实际情况却停不下来
        /**
         * 尽管已经把 canceled 设置成 true，但生产者仍然没有停止，
         * 这是因为在这种情况下，生产者在执行 storage.put(num) 时发生阻塞，
         * 在它被叫醒之前是没有办法进入下一次循环判断 canceled 的值的,
         * 所以在这种情况下用 volatile 是没有办法让生产者停下来的，
         *
         *
         * 相反如果用 interrupt 语句来中断，即使生产者处于阻塞状态，仍然能够感受到中断信号，并做响应处理
         */
        producer.canceled = true;
        System.out.println(producer.canceled);
    }


}

/**
 * 声明了一个生产者 Producer，通过 volatile 标记的初始值为 false 的布尔值 canceled 来停止线程
 */
class Producer implements Runnable{

    volatile  boolean canceled = false;

    private BlockingQueue<Integer> storage;

    public Producer(BlockingQueue<Integer> storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
       int num = 0;
       try {
           // while 的判断语句是 num 是否小于 100000 及 canceled 是否被标记
           while (num < 10000 && !canceled) {
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
           e.printStackTrace();
       }finally {
           System.out.println("生产者结束运行....");
       }
    }
}



/**
 * 消费者 Consumer，它与生产者共用同一个仓库 storage
 */
class Consumer {
    BlockingQueue<Integer> storage;

    public Consumer(BlockingQueue<Integer> storage) {
        this.storage = storage;
    }

    public boolean needMoreNums () {
        if (Math.random() > 0.9) {
            return false;
        }
        return true;
    }
}

