package com.chukun.interview.completableFuture;

/**
 * People 对象
 */
public class People {

    private String id;
    private String name;
    private Integer stat;
    private Integer age;

    public People() {
    }

    public People(String id, String name, Integer stat,Integer age) {
        this.id = id;
        this.name = name;
        this.stat = stat;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}