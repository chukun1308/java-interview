package com.chukun.interview.jvmVariables;

import java.util.concurrent.TimeUnit;

/**
 * @author chukun
 * java 虚拟机基本参数
 *
 * 1.查看参数方法
 *   jps -l  jinfo -flag 参数值 pid
 *
 *   jinfo -flag PrintGCDetails 11832
 *   jinfo -flag MetaspaceSize 11832
 *
 * 2.
 *   查看初始值  java -XX:+PrintFlagsInitial
 *   查看修改的值   java -XX:+PrintFlagsFinal   =表示默认值  :=表示修改过得值
 *   动态修改程序的jvm参数
 *       java -XX:+PrintFlagsFinal -XX:MetaspaceSize=256m  java程序
 *
 *       -Xss 设置单个线程栈的大小  默认 512k-1024k
 *
 *       -XX:MetaspaceSize=256m 设置元空间的大小，注意，使用的是本地内存，默认的大小大概21m
 *
 *       -XX:+PrintGCDetails 打印垃圾回收的细节
 *
 *       -XX:SurvivorRatio 设置新生代 伊甸园区，from区 to区的大小比例
 *        默认: SurvivorRatio=8 Eden:from:to = 8:1:1
 *        SurvivorRatio就是设置伊甸园区占得比例
 *
 *       -XX:NewRatio 配置新生代，老年代在堆结构中的比例，
 *       默认: NewRatio=2 新生代占 1 ,老年代占 2  整个年轻代占堆内存的1/3
 *
 *       -XX:MaxTenuringThreshold 设置垃圾的最大年龄 默认15次
 *       对象在from与to之间互换，超过15次还存活，就会被放入老年代
 * */
public class JVMVariablesOperator {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello JVM gc");
        //TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        //java虚拟机的内存总量
        //long totalMemory = Runtime.getRuntime().totalMemory();
        //java虚拟机试图使用的最大内存总量
        //long maxMemory= Runtime.getRuntime().maxMemory();

        //System.out.println("java虚拟机的内存总量 (-Xms): "+(totalMemory/1024/1024)+"MB");
        //System.out.println("java虚拟机试图使用的最大内存总量 (-Xmx): "+(maxMemory/1024/1024)+"MB");

        byte[] bytes = new byte[20*1024*1024];
    }
}
