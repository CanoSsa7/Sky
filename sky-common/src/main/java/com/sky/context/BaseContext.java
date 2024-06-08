package com.sky.context;

public class
BaseContext {
    //ThreadLocal是独属于一个线程的内存空间，而每一个请求都工作在不同的线程中
    //ThreadLocal只能存一个变量（可以是对象）
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
