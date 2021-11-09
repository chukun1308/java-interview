package com.chukun.interview.callback;

import java.io.Serializable;

/**
 * 定义返回结果
 * @author chukun
 * @version 1.0.0
 */
public class TaskResult implements Serializable {

    /**
     *任务状态
     */
    private Integer taskStatus;

    /**
     * 任务消息
     */
    private String taskMessage;

    /**
     * 任务结果
     */
    private String taskResult;


    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskMessage() {
        return taskMessage;
    }

    public void setTaskMessage(String taskMessage) {
        this.taskMessage = taskMessage;
    }

    public String getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "taskStatus=" + taskStatus +
                ", taskMessage='" + taskMessage + '\'' +
                ", taskResult='" + taskResult + '\'' +
                '}';
    }
}
