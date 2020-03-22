package com.chukun.interview.gc;

/**
 * 查看默认的gc
 *  -XX:+PrintCommandLineFlags
 *
 * -XX:InitialHeapSize=265990592 -XX:MaxHeapSize=4255849472 -XX:+PrintCommandLineFlags
 * -XX:+UseCompressedClassPointers
 * -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation
 * -XX:+UseParallelGC 默认是并行垃圾回收器
 */
public class CheckDefaultGC {

    public static void main(String[] args) {


    }
}
