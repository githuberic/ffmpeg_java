package com.lgq.ffmpeg_java.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lgq
 */
public class ThreadPoolExecutorUtils {

    //多线程
    private static int core = Runtime.getRuntime().availableProcessors();

    public static ExecutorService pool = new ThreadPoolExecutor(core,//核心
            core * 2,//最大
            0L,//空闲立即退出
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),//无边界阻塞队列
            new ThreadPoolExecutor.AbortPolicy());
}
