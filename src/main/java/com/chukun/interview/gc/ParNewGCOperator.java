package com.chukun.interview.gc;

import java.util.Random;

/**
 * 新生代使用 parNewGC
 * -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseParNewGC
 *
 *
 * UseParNewGC 新生代收集器  -----> 会激活 UseSerialOldGC老年代收集器
 *
 * 新生代:
 *      ParNew  parNewGC
 * 老年代:
 *     Tenured  UseSerialOldGC  已经过期了
 */
public class ParNewGCOperator {

    public static void main(String[] args) {

        String str = "useParNewGC";
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
