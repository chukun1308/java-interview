package com.chukun.interview.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author chukun
 *  volatile 修饰标记位适用的场景
 *  run方法里面没有调用阻塞函数，使用volatile线程是可以停止的
 */
public class VolatileCanStop implements Runnable{
    private volatile boolean canceled = false;

    @Override
    public void run() {
       int num = 0;
       try{
       while (!canceled && num < 10000) {
           if (num % 10 == 0) {
               System.out.println(num + " 是10的倍数");
           }
           num++;
           TimeUnit.MILLISECONDS.sleep(1);
       }
       }catch (InterruptedException e) {
           e.printStackTrace();
       }
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileCanStop r = new VolatileCanStop();
        Thread thread = new Thread(r);
        thread.start();
        TimeUnit.MILLISECONDS.sleep(3000);
        r.canceled = true;
    }
}
