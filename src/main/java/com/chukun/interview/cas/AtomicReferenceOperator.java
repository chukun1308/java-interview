package com.chukun.interview.cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子引用，适用于对 对象修改
 * 底层原理cas，与atomicInteger相似
 */

class User {
    String name;
    int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}


public class AtomicReferenceOperator {

    public static void main(String[] args) {

        User z3 = new User("z3", 22);
        User li4 = new User("li4",25);
        AtomicReference<User> reference = new AtomicReference<>();
        reference.set(z3);
        System.out.println(reference.compareAndSet(z3, li4)+"\t the current reference is : "+reference.get().toString());
    }
}
