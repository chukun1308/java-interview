package com.chukun.interview.jvmVariables;

/**
 * @author chukun
 *  对于java中垃圾回收的策略，什么样的对象表示可以被回收?????
 *  可达性分析算法：通过一系列的名为“GC Root”的对象作为起点，
 *  从这些节点向下搜索，搜索所走过的路径称为引用链(Reference Chain)，
 *  当一个对象到GC Root没有任何引用链相连时，则该对象不可达，该对象是不可使用的，
 *  垃圾收集器将回收其所占的内存
 *
 *  在java语言中，可作为GCRoot的对象包括以下几种：
 *
 *     a. java虚拟机栈(栈帧中的本地变量表)中的引用的对象。
 *     b.方法区中的类静态属性引用的对象。
 *     c.方法区中的常量引用的对象。
 *     d.本地方法栈中JNI本地方法的引用对象。
 *
 *    一、堆区:
 *       1.存储的全部是对象，每个对象都包含一个与之对应的class的信息。(class的目的是得到操作指令)
 *       2.jvm只有一个堆区(heap)被所有线程共享，堆中不存放基本类型和对象引用，只存放对象本身
 *     栈区:
 *       1.每个线程包含一个栈区，栈中只保存基础数据类型的对象和自定义对象的引用(不是对象)，对象都存放在堆区中
 *       2.每个栈中的数据(原始类型和对象引用)都是私有的，其他栈不能访问。
 *       3.栈分为3个部分：基本类型变量区、执行环境上下文、操作指令区(存放操作指令)。
 *     方法区:
 *       1.又叫静态区，跟堆一样，被所有的线程共享。方法区包含所有的class和static变量。
 *       2.方法区中包含的都是在整个程序中永远唯一的元素，如class，static变量
 *
 *
 */
public class GCRoot {

//    解释：
//    gc1:是虚拟机栈中的局部变量
//    gc2:是方法区中类的静态变量
//    gc3:是方法区中的常量
//    都可以作为GC Roots 的对象。
    private byte[] byteArray = new byte[100 * 1024 * 1024];

    private static GCRoot gc2;
    private static final GCRoot gc3 = new GCRoot();

    public static void m1(){
        GCRoot gc1 = new GCRoot();
        System.gc();
        System.out.println("第一次GC完成");
    }
    public static void main(String[] args) {
        m1();
    }
}
