package com.chukun.interview.callback;

/**
 * 定义接口回调实现类
 * @author chukun
 * @version 1.0.0
 */
public class TaskHandler implements TaskCallable<TaskResult> {
    @Override
    public TaskResult callable(TaskResult taskResult) {
        // 拿到结果统一处理
        System.out.println(taskResult);
        return taskResult;
    }
}
