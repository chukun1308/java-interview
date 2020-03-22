package com.chukun.interview.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁的基本操作
 *  读写互斥 读读共享 写写互斥
 *  适用于缓存的设计，写互斥，读共享
 */
public class ReadWriteLockOperator {

    public static void main(String[] args) {
        ReadWriteCache cache = new ReadWriteCache();

        //5个线程设置缓存
        for(int i=0;i<5;i++){
            int temp = i;
            new Thread(()->{
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                cache.set(temp+"",temp+"");
                cache.readWriteSet(temp+"",temp+"");
            },String.valueOf(i)).start();
        }
        //5个线程读取缓存
        for(int i=0;i<5;i++){
            int temp = i;
            new Thread(()->{
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                cache.get(temp+"");
                cache.readWriteGet(temp+"");
            },String.valueOf(i)).start();
        }
    }

}

class ReadWriteCache{

    private Map<String,Object> cacheMap = new HashMap<>();

    /**
     * 设置缓存,线程被打断，不是原子操作,synchronized力度太粗
     * @param key
     * @param value
     */
    public void set(String key,Object value){
        System.out.println(Thread.currentThread().getName()+"\t key : "+key +" value : "+value);
        cacheMap.put(key,value);
        System.out.println(Thread.currentThread().getName()+"\t set ok.....");
    }

    /**
     * 获取缓存
     * @param key
     */
    public void get(String key){
        System.out.println(Thread.currentThread().getName()+"\t key : "+key);
        Object result = cacheMap.get(key);
        System.out.println(Thread.currentThread().getName()+"\t value : "+result);
    }

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 设置缓存
     * @param key
     * @param value
     */
    public void readWriteSet(String key,Object value){
        rwLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t key : " + key + " value : " + value);
            cacheMap.put(key, value);
            System.out.println(Thread.currentThread().getName() + "\t set ok.....");
        }finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 获取缓存
     * @param key
     */
    public void readWriteGet(String key){
        rwLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t key : " + key);
            Object result = cacheMap.get(key);
        }finally {
            rwLock.readLock().unlock();
        }
    }
}
