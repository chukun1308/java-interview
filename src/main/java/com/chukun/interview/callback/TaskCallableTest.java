package com.chukun.interview.callback;

/**
 * 测试回调
 * @author chukun
 * @version 1.0.0
 */
public class TaskCallableTest {

    public static void main(String[] args) {
        TaskCallable<TaskResult> taskCallable = new TaskHandler();
        TaskExecutor taskExecutor = new TaskExecutor(taskCallable, "测试回调");
        new Thread(taskExecutor,"测试回调").start();
    }
}
