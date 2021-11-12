package com.chukun.interview.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

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


    }

    /**
     * 正常使用+不需要锁块
     */
    private static void lockSupport() {
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

    /**
     * 正常的wait--->notify可以唤醒
     */
    public static void normalWaitNotify() {
        Object objectLock = new Object(); //同一把锁，类似资源类

        new Thread(() -> {
            synchronized (objectLock) {
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName()+"\t"+"被唤醒了");
        },"t1").start();

        //暂停几秒钟线程
        // try { TimeUnit.SECONDS.sleep(3L); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            synchronized (objectLock) {
                objectLock.notify();
            }
        },"t2").start();
    }

    /**
     * wait方法和notify方法，两个都去掉同步代码块,直接报错
     */
    public static void waitNotifyException01() {
        Object objectLock = new Object(); //同一把锁，类似资源类

        new Thread(() -> {
            // synchronized (objectLock) {
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            //}
            System.out.println(Thread.currentThread().getName()+"\t"+"被唤醒了");
        },"t1").start();

        //暂停几秒钟线程
        // try { TimeUnit.SECONDS.sleep(3L); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            //synchronized (objectLock) {
                objectLock.notify();
            //}
        },"t2").start();
    }

    /**
     * 将notify放在wait方法前先执行，t1先notify了，3秒钟后t2线程再执行wait方法
     * 程序一直无法结束
     * 结论
     * 先wait后notify、notifyall方法，等待中的线程才会被唤醒，否则无法唤醒
     */
    public static void waitNotifyException02() {
        Object objectLock = new Object(); //同一把锁，类似资源类

        new Thread(() -> {
            synchronized (objectLock) {
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName()+"\t"+"被唤醒了");
        },"t1").start();

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(3L); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            synchronized (objectLock) {
            objectLock.notify();
            }
        },"t2").start();
    }

    /**
     * 正常的await--->signal可以唤醒
     */
    public static void normalAwaitSignal() {
        Lock lock = new ReentrantLock(); //同一把锁，类似资源类
        Condition condition = lock.newCondition();
        new Thread(() -> {
              lock.lock();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            System.out.println(Thread.currentThread().getName()+"\t"+"被唤醒了");
        },"t1").start();

        //暂停几秒钟线程
        // try { TimeUnit.SECONDS.sleep(3L); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            lock.lock();
               try {
                   condition.signal();
               } catch (Exception e) {
                   e.printStackTrace();
               }finally {
                   lock.unlock();
               }
        },"t2").start();
    }

    /**
     * await方法和signal方法，两个都去掉lock块,直接报错
     * condition.await();和 condition.signal();都触发了 IllegalMonitorStateException异常
     * lock、unlock对里面才能正确调用调用condition中线程等待和唤醒的方法
     */
    public static void awaitSignalException01() {
        Lock lock = new ReentrantLock(); //同一把锁，类似资源类
        Condition condition = lock.newCondition();
        new Thread(() -> {
            // lock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                // lock.unlock();
            }
            System.out.println(Thread.currentThread().getName()+"\t"+"被唤醒了");
        },"t1").start();

        //暂停几秒钟线程
        // try { TimeUnit.SECONDS.sleep(3L); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            // lock.lock();
            try {
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                // lock.unlock();
            }
        },"t2").start();
    }

    /**
     * 将signal放在await方法前先执行，t1先signal了，3秒钟后t2线程再执行await方法
     * 程序一直无法结束
     * 结论
     * 先await后signal方法，等待中的线程才会被唤醒，否则无法唤醒
     */
    public static void awaitSignalException02() {
        Lock lock = new ReentrantLock(); //同一把锁，类似资源类
        Condition condition = lock.newCondition();
        new Thread(() -> {
            lock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            System.out.println(Thread.currentThread().getName()+"\t"+"被唤醒了");
        },"t1").start();

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(3L); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        },"t2").start();
    }
}
