package com.chukun.interview.completableFuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * completableFuture实战案例
 * @author chukun
 */
public class CompletableFutureAPIAction {


    public static void main(String[] args) {
        compose();
    }


    /**
     * 同一个商品在各个平台上的价格，要求获得一个清单列表，
     * 1 step by step，查完京东查淘宝，查完淘宝查天猫......
     *
     * 2 all   一口气同时查询。。。。。
     */
    static List<NetMall> list = Arrays.asList(
            new NetMall("jd"),
            new NetMall("tmall"),
            new NetMall("pdd"),
            new NetMall("mi")
    );


    /**
     * 同步调用: step by step，查完京东查淘宝，查完淘宝查天猫
     * @param list
     * @param productName
     * @return
     */
    public static List<String> findPriceSync(List<NetMall> list,String productName) {
        return list.stream().map(mall -> String.format(productName+" %s price is %.2f",mall.getNetMallName(),mall.getPriceByName(productName))).collect(Collectors.toList());
    }

    /**
     * 异步调用: all 一口气同时查询
     * @param list
     * @param productName
     * @return
     */
    public static List<String> findPriceAsync(List<NetMall> list,String productName) {
        return list.stream().map(mall -> CompletableFuture.supplyAsync(() -> String.format(productName + " %s price is %.2f", mall.getNetMallName(), mall.getPriceByName(productName)))).collect(Collectors.toList()).stream().map(CompletableFuture::join).collect(Collectors.toList());
    }


    /**
     * thenCompose的传入函数的返回值是CompletableFuture<T>
     * 对计算结果进行合并
     */
    public static void compose()
    {
        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 10;
        }).thenCompose(f -> CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 20 + f;
        })).join());
    }


    /**
     * thenCombine:两个CompletionStage任务都完成后，最终能把两个任务的结果一起交给thenCombine
     * 对计算结果进行合并
     */
    public static void combine()
    {
        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            return 20;
        }), (r1, r2) -> {
            return r1 + r2;
        }).join());
    }


    /**
     *  applyToEither: 谁快用谁
     * 对计算速度进行选用
     */
    public static void calculatorSpeed()
    {
        System.out.println(CompletableFuture.supplyAsync(() -> {
            //暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 2;
        }), r -> {
            return r;
        }).join());

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
    }


    /**
     * thenAccept(Consumer action) :接收任务的处理结果，并消费处理，无返回结果,任务 A 执行完执行 B，B 需要 A 的结果，但是任务 B 无返回值
     * thenRun(Runnable runnable): 任务 A 执行完执行 B，并且 B 不需要 A 的结果
     * thenApply(Function fn): 任务 A 执行完执行 B，B 需要 A 的结果，同时任务 B 有返回值
     * 对计算结果进行消费
     */
    public static void resultConsume()
    {
        CompletableFuture.supplyAsync(() -> {
            return 1;
        }).thenApply(f -> {
            return f+2;
        }).thenApply(f -> {
            return f+3;
        }).thenAccept(r -> System.out.println(r));


        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {}).join());


        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenAccept(resultA -> {}).join());


        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB").join());
    }



    /**
     * thenApply: 计算结果存在依赖关系，这两个线程串行化,由于存在依赖关系(当前步错，不走下一步)，当前步骤有异常的话就叫停。
     * handle: 有异常也可以往下一步走，根据带的异常参数可以进一步处理
     *
     * 对计算结果进行处理
     */
    public static void handleResult()
    {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 1;
        }).handle((f,e) -> {
            System.out.println("-----1");
            return f + 2;
        }).handle((f,e) -> {
            System.out.println("-----2");
            return f + 3;
        }).handle((f,e) -> {
            System.out.println("-----3");
            return f + 4;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("----result: " + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        }).join());

        System.out.println("--------------------------------------------------------");

        System.out.println(CompletableFuture.supplyAsync(() -> {
            return 1;
        }).thenApply(f -> {
            System.out.println("-----1");
            return f + 2;
        }).thenApply(f -> {
            System.out.println("-----2");
            return f + 3;
        }).thenApply(f -> {
            System.out.println("-----3");
            return f + 4;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("----result: " + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        }).join());


        threadPoolExecutor.shutdown();
    }


    /**
     * 获得结果和触发计算
     *  get(): 阻塞
     *  get(long timeout, TimeUnit unit)：带超时的阻塞
     *  getNow(T valueIfAbsent)：没有计算完成的情况下，给一个替代结果，立即获取结果不阻塞
     *  join()：阻塞，与get()方法相同，但是不抛出异常
     *  future.complete(T value)：主动打断，如果打断成功，返回此方法给的默认值，否则返回计算结果
     */
    private static void getResultAndCalculate() throws ExecutionException, InterruptedException {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            //暂停几秒钟线程
            //暂停几秒钟线程
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            return 1;
        },threadPoolExecutor);

        //System.out.println(future.get());
        //System.out.println(future.get(2L,TimeUnit.SECONDS));
        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        //System.out.println(future.getNow(9999));

        System.out.println(future.complete(-44)+"\t"+future.get());


        threadPoolExecutor.shutdown();
    }
}

class NetMall
{

    private String netMallName;

    public String getNetMallName() {
        return netMallName;
    }

    public NetMall(String netMallName)
    {
        this.netMallName = netMallName;
    }

    public double getPriceByName(String productName)
    {
        return calcPrice(productName);
    }

    private double calcPrice(String productName)
    {
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        return ThreadLocalRandom.current().nextDouble() + productName.charAt(0);
    }
}

