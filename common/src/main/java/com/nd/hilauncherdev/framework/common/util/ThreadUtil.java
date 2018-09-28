package com.nd.hilauncherdev.framework.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理工具类
 */
public class ThreadUtil {
	/**
	 * 固定数量线程池
	 */
	private static ExecutorService executorService;

	/**
	 * 其它固定数量线程池
	 */
	private static ExecutorService otherExecutorService;

	/**
	 * 非固定数量线程池
	 */
	private static ExecutorService moreExecutorService;
	
	/**
	 * 该方法为单线程执行仅适用于应用程序图标刷新
	 * @param command
	 */
	public static void execute(Runnable command) {
		if (executorService == null)
			executorService = Executors.newFixedThreadPool(1);
		
		executorService.execute(command);
	}


	/**
	 * 其它固定数量线程池
	 * @param command
	 */
	public static void executeSubmit(Runnable command) {
		if (otherExecutorService == null)
			otherExecutorService = new ThreadPoolExecutor(1, 3,
					2L, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>());
		
		otherExecutorService.execute(command);
	}

	/**
	 * 非固定数量线程池
	 * @param command
	 */
	public static void executeMore(Runnable command) {
		if (moreExecutorService == null)
			moreExecutorService = Executors.newCachedThreadPool();

		moreExecutorService.execute(command);
	}
	
}
