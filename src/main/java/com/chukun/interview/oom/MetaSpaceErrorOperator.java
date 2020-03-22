package com.chukun.interview.oom;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *  元空间溢出
 *  Caused by: java.lang.OutOfMemoryError: Metaspace
 */
public class MetaSpaceErrorOperator {

    static class OOMTest{}

    public static void main(final String[] args) {
        int i = 0;
        try {
           while (true){
               i++;
               Enhancer enhancer = new Enhancer();
               enhancer.setSuperclass(OOMTest.class);
               enhancer.setUseCache(false);
               enhancer.setCallback(new MethodInterceptor() {
                   public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                       return methodProxy.invoke(o,args);
                   }
               });
               enhancer.create();
           }
        }catch ( Throwable a){
            System.out.println("******多少次之后发生异常 : "+i);
            a.printStackTrace();
        }
    }
}
