package com.chukun.interview.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableOperator {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new CallableDemo());

        //futureTask会复用，不会计算第二次
        new Thread(futureTask,"AAA").start();
        new Thread(futureTask,"BBB").start();

        //此方法如果获取不到值，就会阻塞，一般放在方法的处理逻辑最后
        //Integer integer = futureTask.get();

        while (!futureTask.isDone()){
        }
        Integer integer = futureTask.get();
        System.out.println(integer);
    }


}

class CallableDemo implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName()+" \t come in....");
        return 1024;
    }
}
