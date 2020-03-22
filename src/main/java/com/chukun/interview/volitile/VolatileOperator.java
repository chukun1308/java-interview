package com.chukun.interview.volitile;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chukun
 *  volatile 可见性操作
 */
public class VolatileOperator {

    volatile int number = 0;

    public void addNumber(){
        number = 60;
    }

    public void increase(){
        number++;
    }

    AtomicInteger atomicNumber = new AtomicInteger(0);
    public void atomicIncrease(){
        atomicNumber.incrementAndGet();
    }

    public static void main(String[] args) {
        //volatile 不可见的例子
        //volatileUnVisible();
        //非原子性操作
        volatileUnAtomic();
        //原子性操作
        volatileAtomic();
    }

    /**
     * volatile可见性的例子
     */
    private static void volatileUnVisible(){
        VolatileOperator volatileOperator = new VolatileOperator();
        new Thread(()->{
            System.out.printf("%s start.....",Thread.currentThread().getName());
            volatileOperator.addNumber();
            System.out.printf("%s end.....",Thread.currentThread().getName());
            System.out.println("the number is : "+volatileOperator.number);
        },"AAA").start();

        //一直阻塞
        while (volatileOperator.number==0){}

        System.out.println(Thread.currentThread().getName()+" the number is :"+volatileOperator.number);
    }

    /**
     * 非原子性操作
     */
    private static void volatileUnAtomic(){
        VolatileOperator volatileOperator = new VolatileOperator();
        for(int i=0;i<20;i++){
            new Thread(()->{
                for(int j=0;j<1000;j++) {
                    volatileOperator.increase();
                }
            },String.valueOf(i)).start();
        }
        //两个线程 main线程 gc两个
        while (Thread.activeCount()>2){
            Thread.yield();
        }
        System.out.println("main thread number is : "+volatileOperator.number);
    }

    /**
     * atomicInteger实现原子性操作
     */
    private static void volatileAtomic(){
        VolatileOperator volatileOperator = new VolatileOperator();
        for(int i=0;i<20;i++){
            new Thread(()->{
                for(int j=0;j<1000;j++) {
                    volatileOperator.atomicIncrease();
                }
            },String.valueOf(i)).start();
        }
        //两个线程 main线程 gc两个
        while (Thread.activeCount()>2){
            Thread.yield();
        }
        System.out.println("main thread number is : "+volatileOperator.atomicNumber.get());
    }
}
