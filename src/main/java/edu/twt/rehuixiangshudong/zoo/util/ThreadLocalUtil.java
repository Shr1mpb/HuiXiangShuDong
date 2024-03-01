package edu.twt.rehuixiangshudong.zoo.util;

/**
 * 用户的每次访问都是一个独立的线程
 * 此工具类用于在当前线程保存用户的uid
 */
public class ThreadLocalUtil {
    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void setCurrentUid(Integer uid) {
        threadLocal.set(uid);
    }

    public static Integer getCurrentUid() {
        return threadLocal.get();
    }

    public static void removeCurrentUid() {
        threadLocal.remove();
    }
}
