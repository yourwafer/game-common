package com.tw.game.common.utils.codec;

import com.tw.game.common.utils.thread.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class ZlibUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ZlibUtils.class);
	
	/** 默认缓冲区大小 */
	private static final int DEFUALT_BUFFER_SIZE = 1024;
	/** 默认压缩级别 */
	private static final int DEFAULT_LEVEL = 5;
	
	/**
	 * 将指定字符串内容压缩为byte[]
	 * @param src 字符串内容
	 * @return
	 */
	public static byte[] zip(String src) {
		byte[] bytes = src.getBytes(Charset.forName("UTF8"));
		return zip(bytes);
	}
	
	/**
	 * 将输入的byte[]进行压缩
	 * @param src
	 * @return
	 */
	public static byte[] zip(byte[] src) {
		return zip(src, DEFAULT_LEVEL);
	}
	
	/** 解压任务线程池 */
	private static ExecutorService executorService = Executors.newCachedThreadPool(
			new NamedThreadFactory(new ThreadGroup("ZlibUtils"), "解压处理"));

	/**
	 * 将输入的byte[]进行解压
	 * @param src
	 * @return
	 */
	public static byte[] unzip(final byte[] src, long timeout, TimeUnit unit) {
		Future<byte[]> future = executorService.submit(new Callable<byte[]>() {
			@Override
			public byte[] call() throws Exception {
				Inflater inflater = new Inflater();
				inflater.setInput(src);
				
				ByteArrayOutputStream os = new ByteArrayOutputStream(DEFUALT_BUFFER_SIZE);
				byte[] buff = new byte[DEFUALT_BUFFER_SIZE];
				try {
					while (!inflater.finished()) {
						int count = inflater.inflate(buff);
						os.write(buff, 0, count);
					}
					inflater.end();
				} catch (DataFormatException e) {
					String message = "解压时发生数据格式异常";
					logger.error(message, e);
					throw new IllegalArgumentException(message, e);
				} finally {
					try {
						os.close();
					} catch (IOException e) {
						// 永远不会执行的
					}
				}
				return os.toByteArray();
			}
		});
		
		try {
			return future.get(timeout, unit);
		} catch (InterruptedException e) {
			throw new IllegalStateException("解压时被打断:" + e.getMessage());
		} catch (ExecutionException e) {
			throw new IllegalStateException("解压时发生错误:" + e.getMessage());
		} catch (TimeoutException e) {
			throw new IllegalStateException("解压处理超时");
		}
	}
	
	public static byte[] zip(byte[] src, int level) {
		if (level < Deflater.NO_COMPRESSION || level > Deflater.BEST_COMPRESSION) {
			FormattingTuple message = MessageFormatter.format("不合法的压缩等级[{}]", level);
			logger.error(message.getMessage());
			throw new IllegalArgumentException(message.getMessage());
		}
		
		Deflater df = new Deflater(level);
		df.setInput(src);
		df.finish();

		ByteArrayOutputStream baos = new ByteArrayOutputStream(DEFUALT_BUFFER_SIZE);
		byte[] buff = new byte[DEFUALT_BUFFER_SIZE];
		while (!df.finished()) {
			int count = df.deflate(buff);
			baos.write(buff, 0, count);
		}
		df.end();
		
		try {
			baos.close();
		} catch (IOException e) {
			// 永远不会执行的
		}
		return baos.toByteArray();
	}

}
