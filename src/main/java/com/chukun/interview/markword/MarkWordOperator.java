package com.chukun.interview.markword;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * java内存分布
 * java -XX:+PrintCommandLineFlags -version
 * @author chukun
 */
public class MarkWordOperator {


    public static void main(String[] args) {
        //VM的细节详细情况
        System.out.println(VM.current().details());
        //所有的对象分配的字节都是8的整数倍。
        System.out.println(VM.current().objectAlignment());
        System.out.println("-------------------------------------------------------");
        Object obj  = new Object();
        System.out.println( ClassLayout.parseInstance(obj).toPrintable());

    }
}
