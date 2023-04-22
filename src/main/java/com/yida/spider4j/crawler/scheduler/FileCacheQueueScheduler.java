package com.yida.spider4j.crawler.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.scheduler.filter.DuplicateFilter;
import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.io.IOUtils;
import com.yida.spider4j.crawler.utils.log.LogUtils;

/**
 * @ClassName: FileCacheQueueScheduler
 * @Description: 将URL存储到文件里,便于爬虫断点恢复
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 下午6:08:50
 *
 */
public class FileCacheQueueScheduler extends DuplicateFilterScheduler implements MonitorableScheduler {

    private String filePath;

    private String fileUrlAllName = ".urls.txt";

    private Task task;

    private String fileCursor = ".cursor.txt";

    private PrintWriter fileUrlWriter;

    private PrintWriter fileCursorWriter;

    private AtomicInteger cursor = new AtomicInteger();

    private AtomicBoolean inited = new AtomicBoolean(false);

    private BlockingQueue<Request> queue;

    private Set<String> urls;

    public FileCacheQueueScheduler(String filePath) {
    	if(StringUtils.isEmpty(filePath)) {
    		filePath = this.config.getCrawlResumeFolder();
    	}
        if (!filePath.endsWith("/") && !filePath.endsWith("\\")) {
            filePath += "/";
        }
        this.filePath = filePath;
        initDuplicateFilter();
    }

    private void flush() {
        fileUrlWriter.flush();
        fileCursorWriter.flush();
    }

    private void init(Task task) {
        this.task = task;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        readFile();
        initWriter();
        initFlushThread();
        inited.set(true);
        LogUtils.info("init cache scheduler success");
    }

    private void initDuplicateFilter() {
    	setDuplicateFilter(
            new DuplicateFilter() {
                @Override
                public boolean isDuplicate(Request request, Task task) {
                    if (!inited.get()) {
                        init(task);
                    }
                    return !urls.add(request.getUrl());
                }

                @Override
                public void resetDuplicateCheck(Task task) {
                    urls.clear();
                }

                @Override
                public int getTotalRequestsCount(Task task) {
                    return urls.size();
                }
            });
    }

    private void initFlushThread() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                flush();
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private void initWriter() {
        try {
            fileUrlWriter = new PrintWriter(new FileWriter(getFileName(fileUrlAllName), true));
            fileCursorWriter = new PrintWriter(new FileWriter(getFileName(fileCursor), false));
        } catch (IOException e) {
            throw new RuntimeException("init cache scheduler error", e);
        }
    }

    private void readFile() {
        try {
            queue = new LinkedBlockingQueue<Request>();
            urls = new LinkedHashSet<String>();
            readCursorFile();
            readUrlFile();
        } catch (FileNotFoundException e) {
            LogUtils.info("init cache file " + getFileName(fileUrlAllName));
        } catch (IOException e) {
        	LogUtils.error("init file error:\n" + e.getMessage());
        }
    }

    private void readUrlFile() throws IOException {
        String line;
        BufferedReader fileUrlReader = null;
        try {
            fileUrlReader = new BufferedReader(new FileReader(getFileName(fileUrlAllName)));
            int lineReaded = 0;
            while ((line = fileUrlReader.readLine()) != null) {
                urls.add(line.trim());
                lineReaded++;
                if (lineReaded > cursor.get()) {
                    queue.add(new Request(line));
                }
            }
        } finally {
            if (fileUrlReader != null) {
                IOUtils.closeSilence(fileUrlReader);;
            }
        }
    }

    private void readCursorFile() throws IOException {
        BufferedReader fileCursorReader = null;
        try {
        	fileCursorReader = new BufferedReader(new FileReader(getFileName(fileCursor)));
            String line = null;
            //read the last number
            while ((line = fileCursorReader.readLine()) != null) {
                cursor = new AtomicInteger(Integer.valueOf(line));
            }
        } finally {
            if (fileCursorReader != null) {
                IOUtils.closeSilence(fileCursorReader);
            }
        }
    }

    private String getFileName(String filename) {
        return filePath + task.taskId() + filename;
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        queue.add(request);
        fileUrlWriter.println(request.getUrl());
    }

    @Override
    public synchronized Request poll(Task task) {
        if (!inited.get()) {
            init(task);
        }
        fileCursorWriter.println(cursor.incrementAndGet());
        return queue.poll();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        return queue.size();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return getDuplicateFilter().getTotalRequestsCount(task);
    }
}
