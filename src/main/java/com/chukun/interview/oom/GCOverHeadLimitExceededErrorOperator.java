package com.chukun.interview.oom;

import java.util.ArrayList;
import java.util.List;

/**
 *  gc过头，导致应用程序无法正常工作
 *
 *  举例说明:
 *    98%的空间都在gc 只有2%的空间可以使用，
 *    这就导致，刚清理一部分空间，又被占满，导出一直在gc，
 *    回收效果也不明显，抛出 Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
 */
public class GCOverHeadLimitExceededErrorOperator {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        int i =0;
        try{
            while (true){
                list.add(String.valueOf(i++).intern());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw  e;
        }
    }
}
