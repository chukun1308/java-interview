package com.chukun.interview.rxjava;

import com.chukun.interview.completableFuture.People;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author chukun
 *  1. 反应式打印 详见 {@link #reactivePrint()}
 *  2. 顺序调用  详见 {@link #useRpcSync()}
 *  3. 切换 io线程调用 {@link #useRpcSyncSwitchIo()}
 *  4.发射元素 主线程调用  详见 {@link #reactiveConcurrentMain()}
 *  5.发射元素 异步调用  详见 {@link #reactiveConcurrent()}
 *
 *  其他高端操作，详见 https://github.com/ReactiveX/RxJava
 *
 *  Scheduler computation() 在后台运行固定的线程数来计算密集型工作
 *  Scheduler io() 在动态变化的线程集合上运行类 i/o 或阻塞操作 , io线程是守护线程
 *  Scheduler single() 以顺序或FIFO方式在单线程上运行
 *
 */
public class RxJavaExample {


    public static void main(String[] args) throws InterruptedException {

        // reactivePrint();

        // 顺序调用
        // useRpcSync();

        //切换i/o线程执行
        // useRpcSyncSwitchIo();
        // 需要挂起主线程, jvm退出条件，是没有用户线程存在，
        // 所以为了让 io线程执行完毕，需要挂起唯一的用户线程（main线程）
        // Thread.currentThread().join();

        //发射元素线程main执行
        reactiveConcurrentMain();
        //发射元素线程异步执行
        reactiveConcurrent();
    }


    /**
     * reactive 打印 people名称
     */
    private static void reactivePrint() {
        List<People> peoples = makeList();
        // 执行过滤，输出
        // 转换列表为Flowable 流对象
        Flowable.fromArray(peoples.toArray(new People[peoples.size()]))
                // 过滤
                .filter(people -> people.getAge() >= 10)
                // 映射转换
                .map(People::getName)
                // 订阅输出
                .subscribe(System.out::println);
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
     * 模拟rpc 调用
     * @param ip
     * @param param
     * @return
     */
    public static String rpcCall(String ip,String param) {
        System.out.println(Thread.currentThread().getName() +" : " + ip + " rpcCall: " + param);
        try {
            TimeUnit.SECONDS.sleep(1);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return param;
    }


    /**
     * 顺序调用 rpc,这种方式，一直是main线程在调用
     *  main : 192.168.0.0 rpcCall: 192.168.0.0
     * 192.168.0.0
     * main : 192.168.0.1 rpcCall: 192.168.0.1
     * 192.168.0.1
     */
    private static void useRpcSync() {
        List<String> ipList = new ArrayList<>();
        for (int i = 0; i < 10; i++ ) {
            ipList.add("192.168.0."+i);
        }
        // 发起顺序调用
        long start = System.currentTimeMillis();
        // 使用 Flowable.fromArray 方法把 ipList 转换为 Flowable流对象
        Flowable.fromArray(ipList.toArray(new String[ipList.size()]))
                // map 操作符把每个ip 转换为一个新的Flowable流对象
                .map(ip -> rpcCall(ip,ip))
                .subscribe(System.out::println);
        System.out.println("cost: "+(System.currentTimeMillis() - start));
    }

    /**
     * 顺序调用 rpc,这种方式，切换 io线程执行
     *
     * RxCachedThreadScheduler-1 : 192.168.0.0 rpcCall: 192.168.0.0
     * 192.168.0.0
     * RxCachedThreadScheduler-1 : 192.168.0.1 rpcCall: 192.168.0.1
     * 192.168.0.1
     */
    private static void useRpcSyncSwitchIo() {
        List<String> ipList = new ArrayList<>();
        for (int i = 0; i < 10; i++ ) {
            ipList.add("192.168.0."+i);
        }
        // 发起顺序调用
        long start = System.currentTimeMillis();
        // 使用 Flowable.fromArray 方法把 ipList 转换为 Flowable流对象
        Flowable.fromArray(ipList.toArray(new String[ipList.size()]))
                // 切换 io线程执行
                .observeOn(Schedulers.io())
                // map 操作符把每个ip 转换为一个新的Flowable流对象
                .map(ip -> rpcCall(ip,ip))
                .subscribe(System.out::println);
        System.out.println("cost: "+(System.currentTimeMillis() - start));
    }

    /**
     * 发射元素的线程main执行
     * @throws InterruptedException
     */
    private static void reactiveConcurrentMain() throws InterruptedException {
        long start = System.currentTimeMillis();
        Flowable.fromCallable(() -> {
            // 主线程在调用
            System.out.println("current thread: "+ Thread.currentThread().getName());
            // 模拟耗时操作
            TimeUnit.SECONDS.sleep(1);
            return "Done";
        }).observeOn(Schedulers.single())
                .subscribe(System.out::println,Throwable::printStackTrace);

        System.out.println("cost : " + (System.currentTimeMillis()  - start));

        // 等待流结束
        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * 发射元素的线程异步执行
     * @throws InterruptedException
     */
    private static void reactiveConcurrent() throws InterruptedException {
        long start = System.currentTimeMillis();
        Flowable.fromCallable(() -> {
            // 主线程在调用
            System.out.println("current thread: "+ Thread.currentThread().getName());
            // 模拟耗时操作
            TimeUnit.SECONDS.sleep(1);
            return "Done";
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(System.out::println,Throwable::printStackTrace);

        System.out.println("cost : " + (System.currentTimeMillis()  - start));

        // 等待流结束
        TimeUnit.SECONDS.sleep(2);
    }


}
