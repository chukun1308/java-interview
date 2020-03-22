package com.chukun.interview.oom;

import java.util.concurrent.TimeUnit;

/**
 * 不能创建本地线程的错误
 * Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
 *
 * 解决方案:
 *   1. 预估系统是否真的需要这么多线程，如果不是，修改代码，将线程数量降低
 *   2. 对于有些应用确实需要创建多个线程,远超过linux默认1024个线程的限制，修改 linux服务器配置，扩大linux的默认配置
 */
public class UnableCreateNativeThreadErrorOperator {

    public static void main(String[] args) {
        for(int i=0; ; i++){
            System.out.println("****************************** : "+i);
           new Thread(()->{
               try{
                   TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
               }catch (InterruptedException e){
                   e.printStackTrace();
               }
           },""+i).start();
        }
    }

}
