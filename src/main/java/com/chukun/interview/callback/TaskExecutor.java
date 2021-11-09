package com.chukun.interview.callback;

/**
 * 任务执行类
 * @author chukun
 * @version 1.0.0
 */
public class TaskExecutor implements Runnable {

    private TaskCallable<TaskResult> taskCallable;

    private String taskParameter;

    public TaskExecutor(TaskCallable<TaskResult> taskCallable, String taskParameter) {
        this.taskCallable = taskCallable;
        this.taskParameter = taskParameter;
    }

    @Override
    public void run() {
        //TODO 一系列业务逻辑,将结果数据封装成TaskResult对象并返回
        TaskResult taskResult = new TaskResult();
        taskResult.setTaskStatus(1);
        taskResult.setTaskMessage(this.taskParameter);
        taskResult.setTaskResult("异步回调成功");
        taskCallable.callable(taskResult);
    }
}
