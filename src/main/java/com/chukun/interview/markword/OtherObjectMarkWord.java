package com.chukun.interview.markword;

import org.openjdk.jol.info.ClassLayout;

/**
 * 其他对象的内存分布
 * @author chukun
 */
public class OtherObjectMarkWord {

    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(new OtherObject()).toPrintable());
    }
}

class OtherObject {

    long aLong = 10L;
    boolean aBoolean = false;
    char aChar = 'a';
    int anInt = 0;
}
