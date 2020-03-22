package com.chukun.interview.gc;

import java.util.Random;

/**
 * G1垃圾回收器的基本使用
 *  G1垃圾回收期，在物理内存上不分新生代，老年代的区域，它把堆内存分为1-32M不等的小区域(Region)，最多支持64G的内存
 *
 *    Region分为  E (伊甸园区)  S(幸存者区)  O(old区)  H(Humongous区)
 *
 *    Young GC主要是对Eden区进行GC，它在Eden空间耗尽时会被触发。
 *    在这种情况下，Eden空间的数据移动到Survivor空间中，如果Survivor空间不够，
 *    Eden空间的部分数据会直接晋升到年老代空间。Survivor区的数据移动到新的Survivor区中，
 *    也有部分数据晋升到老年代空间中。最终Eden空间的数据为空，GC停止工作，应用线程继续执行。
 *
 *
 *    如果一个对象占用的空间超过了分区容量50%以上，G1收集器就认为这是一个巨型对象。
 *    这些巨型对象，默认直接会被分配在年老代，但是如果它是一个短期存在的巨型对象，
 *    就会对垃圾收集器造成负面影响。为了解决这个问题，G1划分了一个Humongous区，它用来专门存放巨型对象。
 *    如果一个H区装不下一个巨型对象，那么G1会寻找连续的H分区来存储。为了能找到连续的H区，有时候不得不启动Full GC。
 *
 *    -XX:+UseG1GC -Xmx32g -XX:MaxGCPauseMillis=200
 *
 *     -XX:+UseG1GC为开启G1垃圾收集器，
 *     -Xmx32g 设计堆内存的最大内存为32G，
 *     -XX:MaxGCPauseMillis=200设置GC的最大暂停时间为200ms。
 *     如果我们需要调优，在内存大小一定的情况下，我们只需要修改最大暂停时间即可
 *
 *     程序配置如下:
 *
 *       -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseG1GC -XX:MaxGCPauseMillis=200
 *
 */
public class G1GCOperator {

    public static void main(String[] args) {

        String str = "useG1GC";
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
