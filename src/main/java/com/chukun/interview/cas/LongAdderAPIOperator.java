package com.chukun.interview.cas;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * LongAdder只能用来计算加法，且从零开始计算
 * LongAccumulator: long类型的聚合器，需要传入一个long类型的二元操作，可以用来计算各种聚合操作，包括加乘等
 * @author chukun
 */
public class LongAdderAPIOperator {
    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();

        longAdder.increment();
        longAdder.increment();
        longAdder.increment();
        longAdder.increment();

        System.out.println(longAdder.longValue());

        System.out.println("-------------------------------");

        LongAccumulator longAccumulator = new LongAccumulator(Long::sum, 0);

        longAccumulator.accumulate(1);
        longAccumulator.accumulate(1);
        longAccumulator.accumulate(1);
        longAccumulator.accumulate(1);

        System.out.println(longAccumulator.longValue());
    }
}
