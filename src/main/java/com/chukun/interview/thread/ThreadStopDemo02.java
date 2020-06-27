package com.chukun.interview.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author chukun
 *  线程停止
 *   停止线程的方式是使用 interrupt，但 interrupt 仅仅起到通知被停止线程的作用。
 *   而对于被停止的线程而言，它拥有完全的自主权，它既可以选择立即停止，也可以选择一段时间后停止，也可以选择压根不停止
 */
public class ThreadStopDemo02 implements Runnable{
    @Override
    public void run() {
        int count = 0;
        /**
         * 在 StopThread 类的 run() 方法中，首先判断线程是否被中断，然后判断 count 值是否小于 1000。
         * 线程会在每次循环开始之前，检查是否被中断了
         */
        try {
            while (!Thread.currentThread().isInterrupted() && count < 1000) {
                System.out.println("count = " + count);
                count++;
                // sleep 期间能否感受到中断
                TimeUnit.SECONDS.sleep(1000);
            }
        }catch (InterruptedException e) {
            // 即便线程还在休眠，仍然能够响应中断通知，并抛出异常。
            /**
             * 除了将异常声明到方法签名中的方式外，还可以在 catch 语句中再次中断线程。
             * 如代码所示，需要在 catch 语句块中调用 Thread.currentThread().interrupt() 函数。
             * 因为如果线程在休眠期间被中断，那么会自动清除中断信号。如果这时手动添加中断信号，中断信号依然可以被捕捉到
             */
            Thread.currentThread().interrupt();
            // e.printStackTrace();
        }
    }

    /**
     * 主线程休眠 5 毫秒后，通知子线程中断，此时子线程仍在执行 sleep 语句，处于休眠中
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new ThreadStopDemo02());
        thread.start();
        TimeUnit.MILLISECONDS.sleep(5);
        thread.interrupt();
    }
}
