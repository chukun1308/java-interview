package com.chukun.interview.reference;

import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author chukun
 * 弱引用map的基本操作
 */
public class WeakHashMapOperator {

    public static void main(String[] args) {

        commonMap();

        weakMap();

    }

    /**
     * 常规map的操作
     */
    private static void commonMap(){
        //常规map
        Map<Integer,String> map = new HashMap<>();
        Integer key = new Integer(1);
        String value = "hashMap";
        map.put(key,value);

        key = null;
        System.gc();
        System.out.println(map);

        System.out.println("=================================");
    }

    /**
     * weakmap的操作
     */
    private static void weakMap(){
        //weakMap的基本操作
        WeakHashMap<Integer,String> weakHashMap = new WeakHashMap<>();

        Integer weakKey = new Integer(2);
        String weakValue = "weakHashMap";
        weakHashMap.put(weakKey,weakValue);

        weakKey = null;
        //gc就会触发垃圾回收,WeakHashMap就会被回收
        System.gc();
        System.out.println(weakHashMap);
    }
}
