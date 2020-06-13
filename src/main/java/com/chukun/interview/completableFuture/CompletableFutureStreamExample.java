package com.chukun.interview.completableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chukun
 *  CompletableFuture 与 Stream基本操作
 *
 *  1. stream 同步调用 rpc 详见 {@link CompletableFutureStreamExample#useRpcSync()}
 *
 *  2. stream 异步调用 rpc 详见 {@link CompletableFutureStreamExample#useRpcAsync()}
 */
public class CompletableFutureStreamExample {


    public static void main(String[] args) {
        List<People> peoples = makeList();

        useStream(peoples);

        //测试同步rpc
        useRpcSync();

        // 测试异步rpc
        useRpcAsync();

    }

    /**
     * 构造people 集合
     * @return
     */
    private static List<People> makeList() {
        ArrayList<People> list = new ArrayList<>();
        People p1 = new People("001","chukun",0,12);

        list.add(p1);

        p1 = new People("002","pengqinqin",1,7);

        list.add(p1);

        p1 = new People("002","pengqinqin",1,10);

        list.add(p1);

        return list;
    }

    /**
     * 利用stream 过滤people
     * @param peopleList
     */
    private static void useStream(List<People> peopleList) {
        List<String> nameList = peopleList.stream().filter(people -> people.getAge() >= 10)
                .map(People::getName)
                .collect(Collectors.toList());
        nameList.forEach(System.out::println);
    }

    /**
     * 模拟rpc 调用
     * @param ip
     * @param param
     * @return
     */
    public static String rpcCall(String ip,String param) {
        System.out.println(ip + " rpcCall: " + param);
        try {
            TimeUnit.SECONDS.sleep(1);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return param;
    }

    /**
     * 同步调用 rpc
     */
    private static void useRpcSync() {
        List<String> ipList = new ArrayList<>();
        for (int i = 0; i < 10; i++ ) {
            ipList.add("192.168.0."+i);
        }
        // 发起广播调用
        long start = System.currentTimeMillis();
        List<String> result = new ArrayList<>();
        // 轮询每个ip，调用
        for (String ip : ipList) {
            result.add(rpcCall(ip,ip));
        }
        result.forEach(System.out::println);
        System.out.println("cost: "+(System.currentTimeMillis() - start));
    }

    /**
     * 异步调用 rpc
     */
    private static void useRpcAsync() {
        List<String> ipList = new ArrayList<>();
        for (int i = 0; i < 10; i++ ) {
            ipList.add("192.168.0."+i);
        }
        // 发起异步广播调用
        long start = System.currentTimeMillis();
        List<CompletableFuture<String>> futureList = ipList.stream()
                .map(ip -> CompletableFuture.supplyAsync(() -> rpcCall(ip, ip)))
                .collect(Collectors.toList());
        //等待所有的结果调用完毕
        List<String> resultList = futureList.stream().map(future -> future.join())
                .collect(Collectors.toList());
        resultList.forEach(System.out::println);
        System.out.println("cost: "+(System.currentTimeMillis() - start));
    }
}
