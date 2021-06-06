package com.chukun.interview.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chukun
 *  设置completableFuture结果
 */
public class CompletableFutureSetExample {

    // 自定义线程池
    private final static int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    private final static ThreadPoolExecutor BIZ_POOL_EXECUTOR = new ThreadPoolExecutor(AVAILABLE_PROCESSORS,
            AVAILABLE_PROCESSORS * 2 ,1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(5),new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 创建completableFuture
        CompletableFuture<String> future = new CompletableFuture();

        //计算任务
        BIZ_POOL_EXECUTOR.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 设置结果到future
            System.out.println("-------" + Thread.currentThread().getName() +" set value");
            future.complete("hello future");
        });

        // 等待计算结果
        System.out.println("main thread wait future");
        /**
         *  future的get()方法企图获取future的结果，如果future的结果没有被设置，则调用线程会被阻塞
          */
        System.out.println(future.get());

        BIZ_POOL_EXECUTOR.shutdown();
    }
}
