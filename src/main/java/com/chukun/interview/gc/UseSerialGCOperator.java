package com.chukun.interview.gc;

import java.util.Random;

/**
 * 使用串行的gc垃圾收集器
 *  配置如下:
 *  -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseSerialGC
 *
 *  新生代:
 *     DefNew  UseSerialGC
 *  老年代:
 *    Tenured  UseSerialOldGC  已经过期了
 *
 *  UseSerialGC 新生代收集器  -----> 会激活 UseSerialOldGC老年代收集器
 *    Serial收集器:
 *         适用于单机，单核cpu
 *
 */
public class UseSerialGCOperator {

    public static void main(String[] args) {

        String str = "useSerialGC";
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
