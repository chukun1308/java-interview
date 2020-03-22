package com.chukun.interview.threadpool;

import java.util.concurrent.*;

/**
 * 线程池异常处理
 */
public class ThreadPoolExecuteExceptionHandler {

    static ThreadPoolExecutor threadPoolExecutor  = new ThreadPoolExecutor(5, 8, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("导出线程池");
            //设置子线程异常的处理器
            t.setUncaughtExceptionHandler(new ExportThreadPoolUncaughtExceptionHandler());
            return t;
        }
    },new ThreadPoolExecutor.AbortPolicy()){
//        @Override
//        protected void afterExecute(Runnable r, Throwable t) {
//            if(null!=t){
//                System.out.println(t.getMessage()+"方式一");
//                //handle
//            }
//        }
    };


    static class ExportThreadPoolUncaughtExceptionHandler  implements Thread.UncaughtExceptionHandler{

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            if(null!=e){
                System.out.println(e.getMessage()+" 方式三");
            }
        }
    }


    public static void main(String[] args) {
        //1.重写线程池的afterExecute()完成对子线程异常的捕获，处理

//        threadPoolExecutor.execute(() -> {
//            System.out.println(1 / 0);
//        });

        //2.使用submit()提交任务，得到future对象，完成对子线异常的捕获
//        try {
//            Future<?> future = threadPoolExecutor.submit(() -> {
//                System.out.println(1 / 0);
//            });
//            Object o = future.get();
//        }catch (Exception e){
//            System.out.println(e.getMessage()+" 方式二");
//        }

        //3.实现Thread.UncaughtExceptionHandler接口，完成对子线程异常的处理
        threadPoolExecutor.execute(() -> {
            System.out.println(1 / 0);
        });
    }
}
