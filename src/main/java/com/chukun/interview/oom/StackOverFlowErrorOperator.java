package com.chukun.interview.oom;

/**
 * 栈溢出实例
 * 递归深度超过栈的最大容量，就会出现
 *  Exception in thread "main" java.lang.StackOverflowError
 */
public class StackOverFlowErrorOperator {

    public static void main(String[] args) {

        stackOverFlowError();
    }

    public static void stackOverFlowError(){
        stackOverFlowError();
    }
}
