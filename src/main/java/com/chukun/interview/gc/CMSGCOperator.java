package com.chukun.interview.gc;

import java.util.Random;

/**
 * 使用 cms 并发标记清除收集器
 *
 *   -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseConcMarkSweepGC
 *
 *  UseConcMarkSweepGC 老年代收集器  -----> 会激活 parNewGC新生代收集器
 *
 *   注意:
 *      当UseConcMarkSweepGC收集失败，会启用UseSerialOldGC进行标记压缩回收
 *
 *
 *    新生代:
 *        ParNew  parNewGC
 *   老年代:
 *       Tenured   concurrent mark-sweep generation
 *
 *   cms标记清除收集器:
 *       优点: 停顿时间短，适合互联网应用
 *       缺点: 由于并发收集，会占用cpu资源  标记清除，不会压缩空间，会有内存碎片
 *
 *
 */
public class CMSGCOperator {

    public static void main(String[] args) {

        String str = "useConcMarkSweepGC";
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
