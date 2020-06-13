package com.chukun.interview.completableFuture;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chukun
 *  使用 completableFuture 计算异步任务
 */
public class CompletableFutureComputeExample {

    // 自定义线程池
    private final static int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    private final static ThreadPoolExecutor BIZ_POOL_EXECUTOR = new ThreadPoolExecutor(AVAILABLE_PROCESSORS,
            AVAILABLE_PROCESSORS * 2 ,1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(5),new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 基于runAsync系列方法实现无返回值的异步计算
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void runAsync() throws ExecutionException, InterruptedException {

        /**
         * 在默认情况下，runAsync(Runnable runnable)方法是使用整个JVM内唯一的ForkJoinPool.commonPool()线程池来执行异步任务的
         */
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("runAsync over");
        });
        /**
         * 调用返回的future的get()方法企图等待future任务执行完毕，
         * 由于runAsync方法不会有返回值，所以当任务执行完毕后，设置future的结果为null
         */
        System.out.println(future.get());
    }


    /**
     * 基于runAsync系列方法实现无返回值的异步计算
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void runAsyncWithPool() throws ExecutionException, InterruptedException {

        /**
         * 使用 自定义线程池执行异步任务
         */
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("runAsync over");
        },BIZ_POOL_EXECUTOR);
        /**
         * 调用返回的future的get()方法企图等待future任务执行完毕，
         * 由于runAsync方法不会有返回值，所以当任务执行完毕后，设置future的结果为null
         */
        System.out.println(future.get());
    }

    /**
     * 基于supplyAsync系列方法实现有返回值的异步计算
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void  supplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 休眠2s模拟任务计算
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello supplyAsync";
        });
        System.out.println(future.get());
    }

    /**
     * 基于supplyAsync系列方法实现有返回值的异步计算
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void  supplyAsyncWithPool() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 休眠2s模拟任务计算
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello supplyAsync";
        },BIZ_POOL_EXECUTOR);
        System.out.println(future.get());
    }

    /**
     * 基于thenRun实现异步任务A，执行完毕后，激活异步任务B执行
     */
    public static void thenRun() throws ExecutionException, InterruptedException {
        CompletableFuture<String> oneFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello thenRun one";
        });

        // 在future上施加事件，当future计算完成后回调该事件，并返回新future
        CompletableFuture<Void> twoFuture = oneFuture.thenRun(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            System.out.println("---after oneFuture over doSomething---");
        });
        // 3.当oneFuture计算完成后回调该事件，并返回twoFuture，另外，在twoFuture上调用get()方法也会返回null
        System.out.println(twoFuture.get());
    }

    /**
     * 基于thenAccept实现异步任务A，执行完毕后，激活异步任务B执行
     */
    public static void thenAccept() {
        CompletableFuture<String> oneFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello thenRun one";
        });

        oneFuture.thenAccept(s -> {
            try{
                TimeUnit.SECONDS.sleep(2);
            }catch (InterruptedException e) {

            }
            System.out.printf("---after oneFuture over doSomething--- %s\n",s);
        });
    }

    /**
     * 基于thenApply实现异步任务A，执行完毕后，激活异步任务B执行。
     * 这种方式激活的异步任务B是可以拿到任务A的执行结果的，并且可以获取到异步任务B的执行结果
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void thenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<String> oneFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 返回计算结果
            return "hello apply one";
        });

        // 在future上施加事件，当future计算完成后回调该事件，并返回新future
        CompletableFuture<String> twoFuture = oneFuture.thenApply((str) -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {

            }
            return "future two " + str;
        });

        System.out.println(twoFuture.get());
    }

    /**
     * 基于whenComplete设置回调函数，当异步任务执行完毕后进行回调
     * @throws InterruptedException
     */
    public static void whenComplete() throws InterruptedException {
         // 1.创建一个CompletableFuture对象
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 1.1模拟异步任务执行
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 1.2返回计算结果
            return "hello,jiaduo";
        });

        future.whenComplete((result,throwable) -> {
            // 如果没有异常，打印异步任务结果
            if (Objects.isNull(throwable)) {
                System.out.println(result);
            } else {
                // 打印异常堆栈
                System.out.println(throwable.getLocalizedMessage());
            }
        });

        // 挂起当前线程
        Thread.currentThread().join();
    }


}
