package com.chukun.interview.completableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author chukun
 *  CompletableFuture组合运算
 *
 *  1. 基于thenCompose实现当一个CompletableFuture执行完毕后，执行CompletableFuture
 *        详见 {@link CompletableFutureCombineExample#compose }
 *  2. 基于thenCombine实现当两个并发运行的CompletableFuture任务都完成后，使用两者的结果作为参数再执行一个异步任务
 *        详见 {@link CompletableFutureCombineExample#thenCombine()}
 *  3. 基于allOf等待多个并发运行的CompletableFuture任务执行完毕
 *        详见 {@link CompletableFutureCombineExample#allOf()}
 *  4. 基于anyOf等多个并发运行的CompletableFuture任务中有一个执行完毕
 *          详见 {@link CompletableFutureCombineExample#anyOf()}
 *  5. CompletableFuture提供了completeExceptionally方法来处理异常情况
 *          详见 {@link CompletableFutureCombineExample#exception()}
 *
 */
public class CompletableFutureCombineExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        compose();

        thenCombine();

        allOf();

        anyOf();

        exception();
    }

    /**
     * 组合任务
     * 1.调用了thenCompose方法，企图让future1执行完毕后，激活使用其结果作为doSomethingTwo(String companyId)方法的参数的任务。
     */
    public static void compose() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = doSomethingOne("123")
                .thenCompose(id ->
                        doSomethingTwo(id));
        System.out.println(future.get());
    }

    /**
     * 基于thenCombine实现当两个并发运行的CompletableFuture任务都完成后，使用两者的结果作为参数再执行一个异步任务
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void thenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = doSomethingOne("123")
                .thenCombine(doSomethingTwo("456"), (one, two) -> one + "*****" + two);
        System.out.println(future.get());
    }

    /**
     * CompletableFuture提供了completeExceptionally方法来处理异常情况
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void exception() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();

        new Thread(() -> {
            try{
               if (true) {
                   throw new RuntimeException("exception test");
               }
               // 设置结果
                future.complete("ok");
            }catch (Exception e) {
                future.completeExceptionally(e);
            }
        },"thread-1").start();

        // 等待结果
        System.out.println(future.get());

        // 实现当出现异常时返回默认值
        System.out.println(future.exceptionally(t -> "default value").get());
    }

    /**
     * 基于allOf等待多个并发运行的CompletableFuture任务执行完毕
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void  allOf() throws ExecutionException, InterruptedException {
        // 1.创建future列表
        List<CompletableFuture<String>> futureList = new ArrayList<>();
        futureList.add(doSomethingOne("1"));
        futureList.add(doSomethingOne("2"));
        futureList.add(doSomethingTwo("3"));
        futureList.add(doSomethingTwo("4"));

        // 转换多个future为一个
        CompletableFuture<Void> resultFuture =
                CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]));
        // 等待所有的future都执行完毕
        System.out.println(resultFuture.get());
    }


    /**
     * 基于anyOf等多个并发运行的CompletableFuture任务中有一个执行完毕
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void  anyOf() throws ExecutionException, InterruptedException {
        // 1.创建future列表
        List<CompletableFuture<String>> futureList = new ArrayList<>();
        futureList.add(doSomethingOne("1"));
        futureList.add(doSomethingOne("2"));
        futureList.add(doSomethingTwo("3"));
        futureList.add(doSomethingTwo("4"));

        // 转换多个future为一个
        CompletableFuture<Object> resultFuture =
                CompletableFuture.anyOf(futureList.toArray(new CompletableFuture[futureList.size()]));
        // 等待所有的future都执行完毕
        System.out.println(resultFuture.get());
    }

    /**
     * 异步任务1
     * @return
     */
    public static CompletableFuture<String> doSomethingOne(String originId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String uuId = UUID.randomUUID().toString()
                    .replaceAll("-","")
                    .substring(0,10);
            return String.format("%s-%s",uuId,originId);
        });
    }

    /**
     * 异步任务2
     * @return
     */
    public static CompletableFuture<String> doSomethingTwo(String resultId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return resultId + ": baidu";
        });
    }

}
