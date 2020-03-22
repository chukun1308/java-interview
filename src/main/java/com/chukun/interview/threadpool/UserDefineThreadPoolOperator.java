package com.chukun.interview.threadpool;

import java.util.concurrent.*;

/**
 * @author chukun
 *  生产环境一般不会使用 Executors的方式去创建线程池
 *  原因如下:
 *     1.Executors.newSingleThreadExecutor();
 *     单一线程使用的阻塞队列为 new LinkedBlockingQueue<Runnable>()，默认的队列大小为 Integer.MAX_VALUE
 *     这样会使大流量下，任务堆积，容易造成 OOM
 *     public static ExecutorService newSingleThreadExecutor() {
 *         return new FinalizableDelegatedExecutorService
 *             (new ThreadPoolExecutor(1, 1,
 *                                     0L, TimeUnit.MILLISECONDS,
 *                                     new LinkedBlockingQueue<Runnable>()));
 *     }
 *     2.Executors.newFixedThreadPool(5)
 *      原因同单一线程
 *     public static ExecutorService newFixedThreadPool(int nThreads) {
 *         return new ThreadPoolExecutor(nThreads, nThreads,
 *                                       0L, TimeUnit.MILLISECONDS,
 *                                       new LinkedBlockingQueue<Runnable>());
 *     }
 *     3.Executors.newCachedThreadPool()
 *     带缓冲的线程池，默认的最大线程数为Integer.MAX_VALUE,
 *     当任务队列满了，大流量就会创建Integer.MAX_VALUE个线程，容易造成 OOM
 *     public static ExecutorService newCachedThreadPool() {
 *         return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
 *                                       60L, TimeUnit.SECONDS,
 *                                       new SynchronousQueue<Runnable>());
 *     }
 *
 *     对于coreSize,与 maximumPoolSize的设置，一般把任务分为 cpu密集型，IO密集型
 *     对于cpu密集型来说，不希望来回的切换，所以线程数设置为 cpu核心数+1
 *     对于IO密集型来说，  计算公式如下  线程数 = cpu核心数/(1-阻塞系数) 一般阻塞系数在 0.8-0.9之间
 *
 */
public class UserDefineThreadPoolOperator {

    public static void main(String[] args) {
        //创建线程池,使用拒绝策略为ThreadPoolExecutor.AbortPolicy() ,任务满了，就会抛异常，丢弃
//        ExecutorService threadPool = new ThreadPoolExecutor(
//                2,
//                5,
//                1,
//                TimeUnit.SECONDS,
//                new LinkedBlockingDeque<>(3),
//                Executors.defaultThreadFactory(),
//                new ThreadPoolExecutor.AbortPolicy());

        //使用拒绝策略为ThreadPoolExecutor.CallerRunsPolicy()，会把任务返回给调用者
        /**
         * pool-1-thread-2	 execute。。。。。
         * pool-1-thread-1	 execute。。。。。
         * main	 execute。。。。。   main线程在执行
         * pool-1-thread-4	 execute。。。。。
         * pool-1-thread-2	 execute。。。。。
         * pool-1-thread-3	 execute。。。。。
         * pool-1-thread-4	 execute。。。。。
         * pool-1-thread-1	 execute。。。。。
         * pool-1-thread-5	 execute。。。。。
         */
//        ExecutorService threadPool = new ThreadPoolExecutor(
//                2,
//                5,
//                1,
//                TimeUnit.SECONDS,
//                new LinkedBlockingDeque<>(3),
//                Executors.defaultThreadFactory(),
//                new ThreadPoolExecutor.CallerRunsPolicy());

        //使用拒绝策略为ThreadPoolExecutor.DiscardOldestPolicy()，会把最老的未处理的任务丢弃
//        ExecutorService threadPool = new ThreadPoolExecutor(
//                2,
//                5,
//                1,
//                TimeUnit.SECONDS,
//                new LinkedBlockingDeque<>(3),
//                Executors.defaultThreadFactory(),
//                new ThreadPoolExecutor.DiscardOldestPolicy());

        //使用拒绝策略为ThreadPoolExecutor.DiscardOldestPolicy()，会把未处理的任务丢弃
        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                5,
                1,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());

        for(int i=0;i<9;i++){
            threadPool.execute(()->{
                System.out.println(Thread.currentThread().getName()+"\t execute。。。。。");
            });
        }

        threadPool.shutdown();

    }
}
