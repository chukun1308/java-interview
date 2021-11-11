package com.chukun.interview.lock;

import java.util.concurrent.TimeUnit;

/**
 * Synchronized案例分析
 *
 * 当一个线程试图访问同步代码时它首先必须得到锁，退出或抛出异常时必须释放锁。
 *
 *  所有的普通同步方法用的都是同一把锁——实例对象本身，就是new出来的具体实例对象本身,本类this
 *  也就是说如果一个实例对象的普通同步方法获取锁后，该实例对象的其他普通同步方法必须等待获取锁的方法释放锁后才能获取锁。
 *
 *  所有的静态同步方法用的也是同一把锁——类对象本身，就是我们说过的唯一模板Class
 *  具体实例对象this和唯一模板Class，这两把锁是两个不同的对象，所以静态同步方法与普通同步方法之间是不会有竞态条件的
 *  但是一旦一个静态同步方法获取锁后，其他的静态同步方法都必须等待该方法释放锁后才能获取锁。
 *
 *  作用于实例方法，当前实例加锁，进入同步代码前要获得当前实例的锁.
 *  作用于代码块，对括号里配置的对象加锁。
 *  作用于静态方法，当前类加锁，进去同步代码前要获得当前类对象的锁。
 *
 *  synchronized同步代码块：
 *    实现使用的是monitorenter和monitorexit指令
 *    详见: synchronized同步代码块.png
 *
 *  synchronized普通同步方法：
 *    调用指令将会检查方法的ACC_SYNCHRONIZED访问标志是否被设置。
 *    如果设置了，执行线程会将先持有monitor然后再执行方法，
 *    最后在方法完成(无论是正常完成还是非正常完成)时释放 monitor
 *    详见: synchronized普通同步方法.png
 *
 *  synchronized静态同步方法：
 *     ACC_STATIC, ACC_SYNCHRONIZED访问标志区分该方法是否静态同步方法
 *     详见: synchronized静态同步方法.png
 *
 *  可以使用 javap -v ***.class文件反编译，查看class信息
 *
 * @author chukun
 */
public class SynchronizedCase {

    /**
     * 运行结果:
     * -------sendEmail
     * -------sendSMS
     *
     *  解析:
     *   对于普通同步方法，锁的是当前实例对象，通常指this,具体的一部部手机,所有的普通同步方法用的都是同一把锁——实例对象本身，
     *   对于静态同步方法，锁的是当前类的Class对象，如Phone.class唯一的一个模板
     *   对于同步方法块，锁的是 synchronized 括号内的对象
     *
     * @throws InterruptedException
     */
    public static void test03() throws InterruptedException {
        MobilePhone phone01 = new MobilePhone();
        MobilePhone phone02 = new MobilePhone();
        new Thread(() -> {
            phone01.sendEmail01();
        },"AA").start();
        TimeUnit.MILLISECONDS.sleep(200);
        new Thread(() -> {
            phone02.sendEmail02();
        },"BB").start();
    }


    /**
     * 运行结果:
     * -------sendSMS
     * -------sendEmail
     *
     *  解析:
     *   换成两个对象后，不是同一把锁了，情况立刻变化。
     *
     * @throws InterruptedException
     */
    public static void test02() throws InterruptedException {
        MobilePhone phone01 = new MobilePhone();
        MobilePhone phone02 = new MobilePhone();
        new Thread(() -> {
            phone01.sendEmail01();
        },"AA").start();
        TimeUnit.MILLISECONDS.sleep(200);
        new Thread(() -> {
            phone02.sendSMS01();
        },"BB").start();
    }

    /**
     * 运行结果:
     *  -------sendEmail
     *  -------sendSMS
     *  解析:
     *   一个对象里面如果有多个synchronized方法，某一个时刻内，只要一个线程去调用其中的一个synchronized方法了，
     *  其它的线程都只能等待，换句话说，某一个时刻内，只能有唯一的一个线程去访问这些synchronized方法
     *  锁的是当前对象this，被锁定后，其它的线程都不能进入到当前对象的其它的synchronized方法
     *
     * @throws InterruptedException
     */
    public static void test01() throws InterruptedException {
        MobilePhone phone = new MobilePhone();
        new Thread(() -> {
            phone.sendEmail01();
        },"AA").start();
        TimeUnit.MILLISECONDS.sleep(200);
        new Thread(() -> {
            phone.sendSMS01();
        },"BB").start();
    }
}


//资源类
class MobilePhone {

    public  synchronized void sendEmail01() {
        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("-------sendEmail01");
    }

    public synchronized void sendSMS01() {
        System.out.println("-------sendSMS01");
    }

    public  static synchronized void sendEmail02() {
        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("-------sendEmail02");
    }

    public void hello() {
        System.out.println("-------hello");
    }
}
