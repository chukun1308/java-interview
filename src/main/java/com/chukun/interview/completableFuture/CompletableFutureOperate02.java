package com.chukun.interview.completableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author chukun
 *  completableFuture 操作
 */
public class CompletableFutureOperate02 {

    private static List<People> peoples = new ArrayList<>();

    public static void main(String[] args) {

        init();
        run();
    }


    /**
     * 初始化 peoples
     */
    private static void init() {
        People people = null;
        for (int i = 0; i < 10; i++) {
            String str = UUID.randomUUID().toString().replaceAll("-", "");
            people  = new People();
            people.setId(str.substring(0,10));
            people.setName(str.substring(10,18));
            people.setStat(i % 3);
            peoples.add(people);
        }
    }

    /**
     * 执行相关的操作
     */
    private static void run() {
        // 获取ids
        long start = System.currentTimeMillis();
        // 保存Id列表
        List<String> ids = findIds();
        // 拼接操作
        Stream<CompletableFuture<String>> zip = ids.stream().map(i -> {
                CompletableFuture<String> nameTask = findName(i);
                CompletableFuture<Integer> stateTask = findState(i);
                return nameTask
                        .thenCombineAsync(stateTask, (name, state) -> String.format("name=%s***stat=%d", name,
                                state));
            });
        List<CompletableFuture<String>> completableList = zip.collect(Collectors.toList());
        CompletableFuture<String>[] completableFutures =
                    completableList.toArray(new CompletableFuture[completableList.size()]);
        // all done
        CompletableFuture<Void> allDone = CompletableFuture.allOf(completableFutures);
        CompletableFuture<List<String>> resultFutures = allDone.thenApply(v ->
                completableList.stream().map(CompletableFuture::join).collect(Collectors.toList()));
        List<String> result = resultFutures.join();
        System.out.println(result + "耗时: "+(System.currentTimeMillis() - start));
    }

    /**
     * 获取id
     * @return
     */
    private static List<String> findIds() {
        try {
            TimeUnit.SECONDS.sleep(1);
        }catch (Exception e) {}
        return peoples.stream().map(People::getId).collect(Collectors.toList());
    }

    /***
     * 获取名称
     * @return
     */
    private static CompletableFuture<String> findName(String id) {
        List<String> list = peoples.stream().map(People::getId).collect(Collectors.toList());
        return CompletableFuture.supplyAsync(() -> peoples.stream().filter(people -> list.contains(people.getId()))
                .map(People::getName).findFirst().get());
    }

    /**
     * 获取状态
     * @param id
     * @return
     */
    private static CompletableFuture<Integer> findState(String id) {
        List<String> list = peoples.stream().map(People::getId).collect(Collectors.toList());
        return CompletableFuture.supplyAsync(() -> peoples.stream().filter(people -> list.contains(people.getId()))
                .map(People::getStat).findFirst().get());
    }

    /**
     * People 对象
     */
    private static class People {

        private String id;
        private String name;
        private Integer stat;

        public People () {}

        public People (String id,String name,Integer stat) {
            this.id = id;
            this.name = name;
            this.stat = stat;
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
    }
}
