/*
 * Copyright (c) 2015, Xinlong.inc and/or its affiliates. All rights reserved.
 */
package com.yida.spider4j.crawler.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.yida.spider4j.crawler.config.DefaultConfigurable;

/**
 * 线程池
 * @since 1.0
 * @author  Lanxiaowei@citic-finance.com
 * @date    2015-9-28下午5:47:18
 *
 */
public class ThreadPool extends DefaultConfigurable {
	/**线程个数*/
	private int threadNum;
	
	private ExecutorService executorService;
	
	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1000);

    private AtomicInteger threadAlive = new AtomicInteger();

    private ReentrantLock reentrantLock = new ReentrantLock();

    private Condition condition = reentrantLock.newCondition();

    public ThreadPool(int threadNum) {
        this.threadNum = threadNum;
        //this.executorService = Executors.newFixedThreadPool(threadNum);
        //创建一个有界的队列
        this.executorService = new ThreadPoolExecutor(this.config.getIdleThreadOfPool(), 
        		this.config.getMaxThreadOfPool(),
                this.config.getIdleThreadKeepAliveTime(), 
                TimeUnit.MILLISECONDS,queue);
    }

    public ThreadPool(int threadNum, ExecutorService executorService) {
        this.threadNum = threadNum;
        this.executorService = executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public int getThreadAlive() {
        return threadAlive.get();
    }

    public int getThreadNum() {
        return threadNum;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: execute
     * @Description: 执行一个线程
     * @param @param runnable
     * @return void
     * @throws
     */
    public void execute(final Runnable runnable) {
        if (threadAlive.get() >= threadNum) {
            try {
                reentrantLock.lock();
                while (threadAlive.get() >= threadNum) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        threadAlive.incrementAndGet();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {
                    try {
                        reentrantLock.lock();
                        threadAlive.decrementAndGet();
                        condition.signal();
                    } finally {
                        reentrantLock.unlock();
                    }
                }
            }
        });
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: isShutdown
     * @Description: 线程池是否已关闭
     * @param @return
     * @return boolean
     * @throws
     */
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: shutdown
     * @Description: 关闭线程池
     * @param 
     * @return void
     * @throws
     */
    public void shutdown() {
        executorService.shutdown();
    }
}
