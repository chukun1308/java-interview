package com.chukun.interview.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chukun
 *  可重入锁，基本操作
 *  可重入锁指的是可重复可递归调用的锁，
 *  在外层使用锁之后，在内层仍然可以使用，并且不发生死锁（前提得是同一个对象或者class），这样的锁就叫做可重入锁
 *
 *  synchronized/ReentrantLock都是可重入锁
 */
public class ReentrantLockOperator {

    public static void main(String[] args) {
        Phone phone = new Phone();

//        new Thread(()->{
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                phone.sendSms();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        },"t1").start();
//
//        new Thread(()->{
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                phone.sendSms();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        },"t2").start();

        new Thread(phone).start();

        new Thread(phone).start();
    }


}


class Phone implements Runnable{

    Lock lock = new ReentrantLock();

    //验证synchronized可以重入
    public synchronized void sendSms(){
        System.out.println(Thread.currentThread().getName()+"\t sendSms come in.....");
        System.out.println("发短信");
        sendMail();
        System.out.println(Thread.currentThread().getName()+"\t sendSms end .....");
    }

    public synchronized void sendMail(){
        System.out.println(Thread.currentThread().getName()+"\t sendMail come in.....");
        System.out.println("发邮件");
        System.out.println(Thread.currentThread().getName()+"\t sendMail end .....");
    }


    //验证ReentrantLock可以重入
    @Override
    public void run() {
        lock.lock();
        try{
            set();
        }finally {
            lock.unlock();
        }
    }

    public  void set(){
        System.out.println(Thread.currentThread().getName()+"\t set come in.....");
        System.out.println("set....");
        get();
        System.out.println(Thread.currentThread().getName()+"\t set end .....");
    }

    public  void get(){
        System.out.println(Thread.currentThread().getName()+"\t get come in.....");
        System.out.println("get....");
        System.out.println(Thread.currentThread().getName()+"\t get end .....");
    }


}
