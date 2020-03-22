package com.chukun.interview.collection;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author chukun
 * 集合不安全的操作
 *  arrayList() HashSet() HashMap()
 */
public class CollectionNotSafeOperator {

    public static void main(String[] args) {
        //线程不安全
        listNoSafe();
        //线程安全
        listSafe();
    }

    /**
     * list 不安全的操作
     */
    public static void listSafe() {
        //list使用vector实现安全,通过添加synchronized实现线程安全
        //List<String> list = new Vector<>();

        /**
         * synchronized (mutex) {
         *    list.add(index, element);
         * }
         * 通过同步代码块实现
         */
        //List<String> list = Collections.synchronizedList(new ArrayList<>());

        /**
         * public boolean add(E e) {
         *         final ReentrantLock lock = this.lock;
         *         lock.lock();
         *         try {
         *             Object[] elements = getArray();
         *             int len = elements.length;
         *             Object[] newElements = Arrays.copyOf(elements, len + 1);
         *             newElements[len] = e;
         *             setArray(newElements);
         *             return true;
         *         } finally {
         *             lock.unlock();
         *         }
         *     }
         *
         *     通过写时复制技术，实现线程安全
         */
        List<String> list = new CopyOnWriteArrayList<>();
        for(int i=0;i<3;i++){
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }

    /**
     * list 不安全的操作
     */
    public static void listNoSafe() {
        List<String> list = new ArrayList<>();
        for(int i=0;i<3;i++){
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}
