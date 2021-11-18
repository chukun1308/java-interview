package com.chukun.interview.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 原子更新对象中int类型字段的值
 * @author chukun
 * 以一种线程安全的方式操作非线程安全对象的某些字段。
 * 需求：
 * 1000个人同时向一个账号转账一元钱，那么累计应该增加1000元，
 * 除了synchronized和CAS,还可以使用AtomicIntegerFieldUpdater来实现。
 */
public class AtomicIntegerFieldUpdaterOperator {

    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount();

        for (int i = 1; i <=1000; i++) {
            new Thread(() -> {
                bankAccount.transferMoney(bankAccount);
            },String.valueOf(i)).start();
        }

        //暂停毫秒
        try { TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println(bankAccount.money);

    }
}


class BankAccount {

    private String bankName = "CCB";//银行
    public volatile int money = 0;//钱数

    AtomicIntegerFieldUpdater<BankAccount> fieldUpdater  = AtomicIntegerFieldUpdater.newUpdater(BankAccount.class, "money");

    public void transferMoney(BankAccount bankAccount) {
        fieldUpdater.incrementAndGet(bankAccount);
    }
}
