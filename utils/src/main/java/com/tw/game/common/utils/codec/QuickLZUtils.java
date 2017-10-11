package com.tw.game.common.utils.codec;

import java.nio.charset.Charset;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class QuickLZUtils {

    /**
     * 默认压缩级别
     */
    private static final int DEFAULT_LEVEL = 1;

    /**
     * 将指定字符串内容压缩为byte[]
     *
     * @param src 字符串内容
     * @return
     */
    public static byte[] zip(String src) {
        byte[] bytes = src.getBytes(Charset.forName("UTF8"));
        return zip(bytes);
    }

    /**
     * 将输入的byte[]进行压缩
     *
     * @param src
     * @return
     */
    public static byte[] zip(byte[] src) {
        return zip(src, DEFAULT_LEVEL);
    }

    /**
     * 解压任务线程池
     */
    private static ExecutorService executorService = new ThreadPoolExecutor(1, Math.max(1, Runtime.getRuntime()
            .availableProcessors() / 2), 5, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
        ThreadGroup group = new ThreadGroup("QuickLZ解压");
        String prefix = "解压线程 - ";
        AtomicInteger sn = new AtomicInteger();

        public Thread newThread(Runnable r) {
            return new Thread(group, r, prefix + sn.incrementAndGet());
        }
    });

    /**
     * 将输入的byte[]进行解压
     *
     * @param src
     * @return
     */
    public static byte[] unzip(final byte[] src, long timeout, TimeUnit unit) {
        Future<byte[]> future = executorService.submit(new Callable<byte[]>() {
            public byte[] call() throws Exception {
                return QuickLZ.decompress(src);
            }
        });

        try {
            return future.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IllegalStateException("解压时被打断:" + e.getMessage());
        }
    }

    public static byte[] zip(byte[] src, int level) {
        return QuickLZ.compress(src, level);
    }

}
