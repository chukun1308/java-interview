package com.chukun.interview.callback;

/**
 * 定义回调接口
 * @author chukun
 * @version 1.0.0
 */
public interface TaskCallable<T> {

    T callable(T t);
}
