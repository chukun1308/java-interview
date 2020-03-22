package com.chukun.interview.gc;

import java.util.Random;

/**
 * 并行gc的使用
 *  -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseParallelGC
 *
 *  UseParallelGC 新生代收集器  -----> 会激活 UseParallelOldGC老年代收集器
 *
 *  新生代:
 *      PSYoungGen  UseParallelGC
 *  老年代:
 *      ParOldGen  UseParallelOldGC
 *
 *   Parallel收集器:
 *        适用于高吞吐量的应用，对停顿时间不是很在意的后台计算应用
 */
public class ParallelGCOperator {

    public static void main(String[] args) {

        String str = "useParallelGC";
        try {
            while (true) {
                str += str+ new Random().nextInt(111111) + new Random().nextInt(2222222);
                str.intern();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
