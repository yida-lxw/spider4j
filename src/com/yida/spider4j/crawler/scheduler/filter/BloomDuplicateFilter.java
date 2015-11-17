package com.yida.spider4j.crawler.scheduler.filter;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.utils.hash.BloomFilter;
import com.yida.spider4j.crawler.utils.hash.Funnels;

/**
 * @ClassName: BloomDuplicateFilter
 * @Description: 基于Bloom去重算法实现的URL重复过滤器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月12日 下午4:11:34
 *
 */
public class BloomDuplicateFilter implements DuplicateFilter {
	private int expectedInsertions;

    private double fpp;

    private AtomicInteger counter;

    public BloomDuplicateFilter(int expectedInsertions) {
        this(expectedInsertions, 0.01);
    }

    /**
     * @param expectedInsertions the number of expected insertions to the constructed
     * @param fpp the desired false positive probability (must be positive and less than 1.0)
     */
    public BloomDuplicateFilter(int expectedInsertions, double fpp) {
        this.expectedInsertions = expectedInsertions;
        this.fpp = fpp;
        this.bloomFilter = rebuildBloomFilter();
    }

    protected BloomFilter<CharSequence> rebuildBloomFilter() {
        counter = new AtomicInteger(0);
        return BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), expectedInsertions, fpp);
    }

    private final BloomFilter<CharSequence> bloomFilter;

    @Override
    public boolean isDuplicate(Request request, Task task) {
        boolean isDuplicate = bloomFilter.mightContain(getUrl(request));
        if (!isDuplicate) {
            bloomFilter.put(getUrl(request));
            counter.incrementAndGet();
        }
        return isDuplicate;
    }

    protected String getUrl(Request request) {
        return request.getUrl();
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        rebuildBloomFilter();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return counter.get();
    }
}
