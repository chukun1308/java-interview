package com.chukun.interview.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;

/**
 * <p>lockSupport的简单使用。<p/>
 *   <p>{@link LockSupport#park()} 相当于 Object的wait()方法,Condition的await()方法</p>
 *   <p>{@link LockSupport#unpark(Thread)} ()} 相当于 Object的notify()方法，Condition的signal()方法<p/>
 *
 *   <p>Object方法的注意点：<p/>
 *       <p>1.wait()方法与notify()方法必须放在同步代码块，或者同步方法中。<p/>
 *       <p>2.wait()方法与notify()方法必须放在同步代码块，存在如果notify方法先执行，那么wait的线程就不能被唤醒了.<p/>
 *
 *
 *   <p>Condition方法的注意点：<p/>
 *       <p>1.await()方法与signal()方法必须放在lock锁块中。<p/>
 *       <p>2.await()方法与signal()方法必须放在同步代码块，存在如果signal方法先执行，那么await的线程就不能被唤醒了.<p/>
 *
 * LockSupport类似于Semaphore发放许可证的机制。
 * LockSupport#unpark()方法，就是发放许可证，但是只能发放一个，调用多次也是发放一个。
 *  <p>
 *      Thread a = new Thread(() -> {
 *             System.out.println(Thread.currentThread().getName() +"\t" + "进入....");
 *             LockSupport.park();
 *             LockSupport.park();
 *             System.out.println(Thread.currentThread().getName() +"\t" + "被唤醒了....");
 *
 *         }, "A");
 *         a.start();
 *
 *         new Thread(() -> {
 *             System.out.println(Thread.currentThread().getName() +"\t" + "唤醒....");
 *             LockSupport.unpark(a); // 第一次调用
 *              LockSupport.unpark(a);// 第二次调用
 *         }, "B").start();
 *
 *      上面的代码中，虽然多次调用unpark方法，但是a线程还是会阻塞，原因就是unpark不管调用多少次，只会发放一个许可证。
 * <p/>
 *
 *  LockSupport具体的原理，是由jdk底层的Unsafe类实现的，可以参考底层的openJDK源码.
 *
 * @author chukun
 * @see Object#wait()
 * @see Object#notify()
 * @see Condition#await()
 * @see Condition#signal()
 * @see LockSupport
 * @see sun.misc.Unsafe
 */
public class LockSupportDemo {

    public static void main(String[] args) {

        Thread a = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() +"\t" + "进入....");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() +"\t" + "被唤醒了....");

        }, "A");
        a.start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() +"\t" + "唤醒....");
            LockSupport.unpark(a);
        }, "B").start();
    }
}
