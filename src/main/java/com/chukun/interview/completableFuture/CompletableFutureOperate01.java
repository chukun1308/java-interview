package com.chukun.interview.completableFuture;

import java.util.concurrent.*;

/**
 * @author chukun
 *   future 基本实例
 *
 *   通过第一个例子，我们知道Future接口提供了方法来检测异步计算是否已经结束（使用
 *     isDone方法），等待异步操作结束，以及获取计算的结果。但是这些特性还不足以让你编写简洁
 *     的并发代码。比如，我们很难表述Future结果之间的依赖性；从文字描述上这很简单，“当长时
 *     间计算任务完成时，请将该计算的结果通知到另一个长时间运行的计算任务，这两个计算任务都
 *     完成后，将计算的结果与另一个查询操作结果合并”
 *
 *
 *     将两个异步计算合并为一个——这两个异步计算之间相互独立，同时第二个又依赖于第
 * 一个的结果。
 *     等待Future集合中的所有任务都完成。
 *     仅等待Future集合中最快结束的任务完成（有可能因为它们试图通过不同的方式计算同
 * 一个值），并返回它的结果
 */
public class CompletableFutureOperate01 {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 向ExecutorService提交一个Callable对象
        long start = System.currentTimeMillis();
        Future<Double> future = executorService.submit((Callable<Double>) () -> {
            TimeUnit.SECONDS.sleep(1);
            return 0.5d;
        });
        doSomeElseThing();
        try{
            // 使用重载版本的get方法，它接受一个超时的参数
            Double aDouble = future.get(1, TimeUnit.SECONDS);
            System.out.println(aDouble);
            System.out.println(System.currentTimeMillis()  - start);
        }catch (ExecutionException e) {
             // 计算抛出一个异常
        }catch (InterruptedException e) {
            // 当前线程在等待过程中被中断
        }catch (TimeoutException e) {
           // 在Future对象完成之前超过已过期
        }

    }

    private static void doSomeElseThing() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
    }
}
