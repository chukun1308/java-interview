package com.chukun.interview.completableFuture;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 为了实现最佳价格查询器应用，让我们从每个商店都应该提供的API定义入手。首先，商店
 * 应该声明依据指定产品名称返回价格的方法
 */
public class Shop {

    private String shopName;

    public Shop(String shopName) {
        this.shopName = shopName;
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * 异步获取
     * @param product
     * @return
     */
    public Future<Double> getPriceAsync(String product) {
        // 创建CompletableFuture 对象， 它会包含计算的结果
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        // 线程中以异步方式执行计算
        new Thread(() -> {
            // 需长时间计算的任务结束并得出结果时，设置Future的返回值
            double price = calculatePrice(product);
            futurePrice.complete(price);
        }).start();
        // 无需等待还没结束的计算，直接返回Future对象
        return  futurePrice;
    }

    public Future<Double> getExceptionPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread( () -> {
            try {
                // 如果价格计算正常结束，完成Future操作并设置商品价格
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception ex) {
                //否则就抛出导致失败的异常，完成这次Future操作
                futurePrice.completeExceptionally(ex);
            }
        }).start();
        return futurePrice;
    }

    /**
     * 使用工厂方法supplyAsync创建CompletableFuture
     *
     * supplyAsync方法接受一个生产者（ Supplier）作为参数，返回一个CompletableFuture
     * 对象，该对象完成异步执行后会读取调用生产者方法的返回值。生产者方法会交由ForkJoinPool
     * 池中的某个执行线程（ Executor）运行，但是你也可以使用supplyAsync方法的重载版本，传
     * 递第二个参数指定不同的执行线程执行生产者方法
     *
     * @param product
     * @return
     */
    public Future<Double> getFactoryPriceAsync(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }

    /**
     * 批量获取价格,同步调用
     * @param product
     * @param shops
     * @return
     */
    public List<String> findPrices(String product,List<Shop> shops) {
        return shops.stream()
                .map(shop -> String.format("%s price is %.2f",
                        shop.getShopName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    public void delay() {
        try {
            TimeUnit.SECONDS.sleep(1);
        }catch (InterruptedException e) {
            throw  new RuntimeException(e);
        }
    }

    private double calculatePrice(String product) {
        Random random = new Random();
        delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    /**
     * 执行更多任务，比如查询其他商店
     */
    private static void doSomeElseThing() {
        try {
            TimeUnit.SECONDS.sleep(1);
        }catch (InterruptedException e) {
            throw  new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Shop shop = new Shop("shop01");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        long invocationTime = ((System.nanoTime() - start) / 1000000);
        System.out.println("Invocation returned after " + invocationTime
                + " msecs");
        // 执行更多任务，比如查询其他商店
        doSomeElseThing();
        // 在计算商品价格的同时
        try {
            // 从Future对象中读取价格，如果价格未知，会发生阻塞
            double price = futurePrice.get();
            System.out.printf("Price is %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs");

        System.out.println("====================================================");
        List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
                new Shop("LetsSaveBig"),
                new Shop("MyFavoriteShop"),
                new Shop("BuyItAll"));

        List<String> prices = shop.findPrices("my favorite product", shops);
        System.out.println(prices);

    }
}
