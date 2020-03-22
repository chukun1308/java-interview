package com.chukun.interview.threadpool;

/**
 * @author 死锁案例演示
 * 以及出现死锁怎么排查????
 *  1.先使用 jps -l 查看java进程
 *   如下:
 *    18544 org.jetbrains.jps.cmdline.Launcher
 *    13076 org.jetbrains.kotlin.daemon.KotlinCompileDaemon
 *    1000 com.chukun.interview.threadpool.DeadLockOperator  ---->找到本进程
 *    21196 sun.tools.jps.Jps
 *
 *   2.在使用jstack pid  如: jstack 1000
 *     看到如下信息:
 *       Java stack information for the threads listed above:
 *       ===================================================
 *     "thread-BBB":
 *         at com.chukun.interview.threadpool.DeadHoldLock.run(DeadLockOperator.java:33)
 *         - waiting to lock <0x000000076b89db08> (a java.lang.String)
 *         - locked <0x000000076b89db40> (a java.lang.String)
 *         at java.lang.Thread.run(Thread.java:748)
 *      "thread-AAA":
 *         at com.chukun.interview.threadpool.DeadHoldLock.run(DeadLockOperator.java:33)
 *         - waiting to lock <0x000000076b89db40> (a java.lang.String)
 *         - locked <0x000000076b89db08> (a java.lang.String)
 *         at java.lang.Thread.run(Thread.java:748)
 *
 *     Found 1 deadlock.
 *
 */
public class DeadLockOperator {

    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";
        new Thread(new DeadHoldLock(lockA,lockB),"thread-AAA").start();
        new Thread(new DeadHoldLock(lockB,lockA),"thread-BBB").start();
    }

}

class DeadHoldLock implements Runnable{

    private String lockA;
    private String lockB;

    public DeadHoldLock(String lockA,String lockB){
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName()+"\t 获取到了 "+lockA+" 尝试获取 : "+lockB);
            synchronized (lockB){
                System.out.println(Thread.currentThread().getName()+"\t 获取到了 "+lockB);
            }
        }
    }
}
