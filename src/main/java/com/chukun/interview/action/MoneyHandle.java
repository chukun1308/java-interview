package com.chukun.interview.action;

public class MoneyHandle {


    /**
     *
     * @param outAccount 转出账号
     * @param inAccount 转入账号
     * @param money  余额
     * @param impdent 幂等号
     */
    public void handle(String outAccount, String inAccount, int money, String impdent) {
          // 1. 获取分布锁
        try{
            if (distributeLock()) {
               if (hasHandled(impdent)) {
                   return;
               }
               // 获取数据的当前账号的余额,查询数据库
                int balance =  1000;
               if (balance >= money) {
                   balance -= money;
               }
                int anotherBalance = 0;
               try {
                   // 向inAccount 账户转入金额
                  anotherBalance += money ;
                  // 更新 inAccount的金额数据
               }catch (Exception e) {
                   // 出错回滚
               }
               // 在更新当前outAccount的余额
            }
        }finally {
            unLock();
        }
    }

    public  boolean distributeLock() {
        // 获取分布式锁
        return true;
    }

    public  boolean unLock() {
        // 解除分布式锁
        return true;
    }

    public boolean hasHandled(String impdent) {
        // 判断网络等原因，是否已经处理过了
        return true;
    }
}


