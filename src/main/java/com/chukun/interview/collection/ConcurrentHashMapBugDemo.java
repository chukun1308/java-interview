package com.chukun.interview.collection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chukun
 *  concurrentHashMap 1.8 bug 测试
 *  java 8 测试，出现 死循环
 */
public class ConcurrentHashMapBugDemo {

    public static void main(String[] args) {
        System.out.println("ConcurrentHashMapBugDemo 测试开始....");
        Map<String, Integer> map = new ConcurrentHashMap<>(16);
        /**
         * key "AaAa"  --->  2031775
         * key "BBBB"  --->  2031775
         * computeIfAbsent key相同，会出现 死循环的问题
         *   ConcurrentHashMap computeIfAbsent bug :
         *    1. spread(key.hashCode()) 计算 key值 AaAa ---> 2031775
         *    2. 空 map 进行 initTable
         *    3. casTabAt(tab, i, null, r) 进行 cas 操作，先用 r（即 ReservationNode）进行一个占位的操作
         *    4. mappingFunction.apply(key) 执行函数操作,在本例中会操作实现的函数， 会触发另一次 computeIfAbsent 操作
         *    5. BBBB 进来了，算出来的 h 值也是 2031775
         *    6. 之前 casTabAt(tab, i, null, r) 已经被ReservationNode占用了，不为空了
         *    7. 判断 (fh = f.hash) == MOVED 也不会进行扩容  当前的hash 值为 -3
         *    8. f instanceof TreeBin 也不属于  红黑树，不会进入分支
         *    9. binCount 的值为0 也不会进入分支
         *    10.第二次进来，就一直在loop,死循环了。
         *
         *    解决方案:
         *      在使用 computeIfAbsent之前，先get获取一下，为 null，在执行 putIfAbsent 函数操作，可以达到同样的效果
         */
        map.computeIfAbsent(
                "AaAa",
                key -> {
                    return map.computeIfAbsent(
                            "BBBB",
                            key2 -> 44
                    );
                }
         );
        System.out.println("ConcurrentHashMapBugDemo 测试结束....");
        System.out.println("map content : " + map);
        System.out.println("ConcurrentHashMapBugDemo 解决.....");
        Integer value01 = map.get("AaAa");
        if (value01 == null) {
            value01  = map.putIfAbsent("AaAa", 44);
        }
        System.out.println("ConcurrentHashMapBugDemo 解决完毕");
        System.out.println("map content :" + map);
    }
}
