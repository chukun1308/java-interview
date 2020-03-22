package com.chukun.interview.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author
 *   利用原子引用，实现一个自旋锁
 *   借鉴AtomicInteger底层的cas思想
 *   public final int getAndAddInt(Object var1, long var2, int var4) {
 *         int var5;
 *         do {
 *             var5 = this.getIntVolatile(var1, var2);
 *         } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
 *         return var5;
 *     }
 */
public class WriteSpinLock {

    public static void main(String[] args) {
        SpinLock spinLock = new SpinLock();

        new Thread(()->{
            spinLock.spinLock();
            try {
                System.out.println(Thread.currentThread().getName()+"\t get the spin lock");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLock.spinUnLock();
        },"t1").start();

        new Thread(()->{
            spinLock.spinLock();
            try {
                System.out.println(Thread.currentThread().getName()+"\t get the spin lock");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLock.spinUnLock();
        },"t2").start();
    }

}

/**
 * 利用AtomicReference的原子引用，实现自旋锁
 */
class SpinLock {

    private AtomicReference<Thread> reference = new AtomicReference<>();

    /**
     * 加锁
     */
    public void spinLock(){
        Thread current = Thread.currentThread();
        for(;;){
            if(reference.compareAndSet(null,current)) {
                break;
            }else{
                continue;
            }
        }
    }
    /**
     * 解锁
     */
    public void spinUnLock(){
        Thread current = Thread.currentThread();
        reference.compareAndSet(current,null);
    }

}
