package com.chukun.interview.action;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CyclePrinter {

    volatile  int num =1;
    Lock lock = new ReentrantLock();
    Condition conditionA = lock.newCondition();
    Condition conditionB = lock.newCondition();
    public static void main(String[] args) {

        CyclePrinter test03 = new CyclePrinter();

        Thread thread01 = new Thread(() -> {
            for(int i =0; i< 100; i++) {
                test03.test01();
            }
        },"Printer1");
        Thread thread02 = new Thread(() ->{
            for(int i =0; i< 100; i++) {
                test03.test02();
            }
        },"Printer2");

        thread01.start();
        thread02.start();
    }

    private void test01() {
        lock.lock();
        try {
            if (num % 2 == 0) {
                conditionA.await();
            }
            System.out.println(Thread.currentThread().getName() + "-" + num);
            num++;
            conditionB.signal();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    private void test02() {
        lock.lock();
        try {
            if (num % 2 != 0) {
                conditionB.await();
            }
            System.out.println(Thread.currentThread().getName() + "-" + num);
            num++;
            conditionA.signal();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }




        public static void restoreString() {
            int[] array = {3,2,15,9,13};
            for(int i=0;i<array.length-1;i++){
                for(int j=0;j<array.length-1-i;j++){
                    int temp=array[j];
                    if(array[j]<array[j+1]){
                        array[j]=array[j+1];
                        array[j+1]=temp;
                    }
                }
            }
            System.out.print(Arrays.toString(array));
        }


}
