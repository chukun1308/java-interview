package com.chukun.interview.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * aba问题的解决方案
 * 通过AtomicStampedReference版本控制，解决ABA问题
 *  类似于github的提交的版本号
 */
public class CasABASolution {

    public static void main(String[] args) {

        //通过添加版本号的方式，解决ABA问题
        AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(10,1);

        new Thread(()->{
            int stamp = stampedReference.getStamp();
            try {
                TimeUnit.SECONDS.sleep(1);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" the value is : "+stampedReference.getReference()+" the version is : "+stamp);
            System.out.println(Thread.currentThread().getName()+"  "+stampedReference.compareAndSet(10,5,stamp,stamp+1)
                    +" the value is : "+stampedReference.getReference()
                    +" the current version is : "+stampedReference.getStamp());
            System.out.println(Thread.currentThread().getName()+"  "+stampedReference.compareAndSet(5,10,stampedReference.getStamp(),
                        stampedReference.getStamp()+1)
                    +" the atomicInteger value is : "+stampedReference.getReference()
                    +" the current version is : "+stampedReference.getStamp());
        },"t1").start();

        new Thread(()->{
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName()+" the  value is : "+stampedReference.getReference()+" the version is : "+stamp);
            //睡眠三秒，保证t1线程已经设置完成
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"  "+stampedReference.compareAndSet(10,2019,stamp,stampedReference.getStamp()+1)
                    +" the  value is : "+stampedReference.getReference()
                    +" the  version is : "+stampedReference.getStamp());
        },"t2").start();
    }
}
