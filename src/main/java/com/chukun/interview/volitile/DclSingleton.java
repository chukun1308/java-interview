package com.chukun.interview.volitile;

import java.util.Objects;

/**
 * @author chukun
 *  dcl 的双向锁检查，实现单例模式
 */
public class DclSingleton {

    private static volatile  DclSingleton instance = null;

    private DclSingleton(){
        System.out.println("DclSingleton construct invoked......");
    }

    /**
     * 双端检查的单例模式
     * @return
     */
    public static DclSingleton getInstance(){
        if(Objects.isNull(instance)){
            synchronized (DclSingleton.class){
                if(Objects.isNull(instance)){
                    /**
                     * 这里会出现指令重排序
                     *  对象的初始化过程分为三步
                     *   1. memory = allocate()
                     *   2. init(memory)
                     *   3. instance = memory
                     *
                     *   原因分析:
                     *   其中 2,3 可能会出现 指令重排序
                     *   这样就会导致 有可能 单例的instance会出现没初始化的情况，
                     *   简单的说，就是分配了内存，内存上没引用对象
                     */
                    instance = new DclSingleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {

        System.out.println(DclSingleton.getInstance()==DclSingleton.getInstance());
        System.out.println(DclSingleton.getInstance()==DclSingleton.getInstance());

    }
}
