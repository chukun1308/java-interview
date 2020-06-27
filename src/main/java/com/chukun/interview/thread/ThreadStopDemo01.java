package com.chukun.interview.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author chukun
 *  线程停止
 *   停止线程的方式是使用 interrupt，但 interrupt 仅仅起到通知被停止线程的作用。
 *   而对于被停止的线程而言，它拥有完全的自主权，它既可以选择立即停止，也可以选择一段时间后停止，也可以选择压根不停止
 *
 *   错误的停止方法：
 *    比如 stop()，suspend() 和 resume()，这些方法已经被 Java 直接标记为 @Deprecated。
 *    如果再调用这些方法，IDE 会友好地提示，不应该再使用它们了。但为什么它们不能使用了呢？
 *
 *    1. stop() 会直接把线程停止，这样就没有给线程足够的时间来处理想要在停止前保存数据的逻辑，
 *    任务戛然而止，会导致出现数据完整性等问题.
 *
 *    2.对于 suspend() 和 resume() 而言，
 *    它们的问题在于如果线程调用 suspend()，它并不会释放锁，就开始进入休眠，
 *    但此时有可能仍持有锁，这样就容易导致死锁问题，因为这把锁在线程被 resume() 之前，是不会被释放的
 */
public class ThreadStopDemo01 implements Runnable{
    @Override
    public void run() {
        int count = 0;
        /**
         * 在 StopThread 类的 run() 方法中，首先判断线程是否被中断，然后判断 count 值是否小于 1000。
         * 线程会在每次循环开始之前，检查是否被中断了
         */
        while (!Thread.currentThread().isInterrupted() && count < 1000) {
            System.out.println("count = "+count);
            count++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new ThreadStopDemo01());
        thread.start();
        TimeUnit.MILLISECONDS.sleep(5);
        thread.interrupt();
    }
}
