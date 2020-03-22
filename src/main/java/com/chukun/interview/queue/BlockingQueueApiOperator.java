package com.chukun.interview.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author  chukun
 * 阻塞队列的基本使用
 */
public class BlockingQueueApiOperator {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);

        //第一组api 队列满了，添加抛异常  队列为空，获取也抛异常
        System.out.println(blockingQueue.add("a"));
        System.out.println(blockingQueue.add("b"));
        System.out.println(blockingQueue.add("c"));
        //添加超过队列边界，直接抛异常  java.lang.IllegalStateException: Queue full
        //System.out.println(blockingQueue.add("d"));


        //获取头元素
        System.out.println(blockingQueue.element());

        //出队操作
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        //java.util.NoSuchElementException
        //System.out.println(blockingQueue.remove());

        System.out.println("======================================");

        //第二组api 特殊值，队列满了，入队返回false  队列为空，出队返回 null

        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        //false
        System.out.println(blockingQueue.offer("x"));

        //获取头元素
        System.out.println(blockingQueue.peek());

        //出队操作
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        // null
        System.out.println(blockingQueue.poll());

        //第三组api 队列满了，入队阻塞  队列为空，出队阻塞

        blockingQueue.put("a");
        blockingQueue.put("b");
        blockingQueue.put("c");
        //直接阻塞
        //blockingQueue.put("x");

        //出队操作
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        System.out.println(blockingQueue.take());
        // 直接阻塞
        //System.out.println(blockingQueue.take());

        //第四组api 超时阻塞策略，队列满了，入队阻塞超时时间  队列为空，出队阻塞超时时间

        blockingQueue.offer("a",3, TimeUnit.SECONDS);
        blockingQueue.offer("b",3, TimeUnit.SECONDS);
        blockingQueue.offer("c",3, TimeUnit.SECONDS);
        //阻塞三秒，如果入队失败，则返回false
        blockingQueue.offer("x",3, TimeUnit.SECONDS);

        //出队操作
        System.out.println(blockingQueue.poll(3, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(3, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(3, TimeUnit.SECONDS));
        // 阻塞三秒,如果出队失败，则返回null
        System.out.println(blockingQueue.poll(3, TimeUnit.SECONDS));
    }


}
