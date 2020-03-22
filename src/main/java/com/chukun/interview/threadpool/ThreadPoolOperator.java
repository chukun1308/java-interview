package com.chukun.interview.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池，参数介绍
 *
 *  public ThreadPoolExecutor(int corePoolSize,
 *                               int maximumPoolSize,
 *                               long keepAliveTime,
 *                               TimeUnit unit,
 *                               BlockingQueue<Runnable> workQueue,
 *                               RejectedExecutionHandler handler) {
 *         this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
 *              Executors.defaultThreadFactory(), handler);
 *     }
 *
 *      corePoolSize 代表线程池的核心线程数
 *      maximumPoolSize 代表线程池的最大线程数
 *      keepAliveTime  线程的存活时间，超过这个时间，默认会把超过corePoolSize的线程销毁，恢复到corePoolSize个线程
 *      unit  阻塞时间单位
 *      workQueue  阻塞队列，当任务多余corePoolSize，会先丢入阻塞队列中，当阻塞队列满了，就会创建线程到maximumPoolSize个
 *      handler 当线程池满了，新任务的处理策略
 */
public class ThreadPoolOperator {

    public static void main(String[] args) {

        //创建单线程的线程池
//        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //固定数量的线程池
//        ExecutorService executorService = Executors.newFixedThreadPool(5);

        /**
         * 带缓存的线程池，适合，任务时间不太长的任务，
         * 内部的队列实现为SynchronousQueue，这个队列，生产一个消费一个，会阻塞
         */
        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }

    }
}
