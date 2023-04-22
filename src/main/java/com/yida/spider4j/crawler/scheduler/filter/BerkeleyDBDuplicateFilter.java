package com.yida.spider4j.crawler.scheduler.filter;

import java.util.List;

import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.utils.common.Serializer;
import com.yida.spider4j.crawler.utils.db.BerkeleyDBUtils;

/**
 * @ClassName: BerkeleyDBDuplicateFilter
 * @Description: 基于BerkeleyDB实现的URL去重过滤器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 上午11:12:40
 *
 */
public class BerkeleyDBDuplicateFilter implements DuplicateFilter {
	private BerkeleyDBUtils dbUtils;
	
	@Override
	public boolean isDuplicate(Request request, Task task) {
		//生成MD5摘要,唯一标识每个request对象,用于去重
		String checksum = Serializer.checksumHex(request);
		byte[] key = checksum.getBytes();
		dbUtils.openDB();
		Request req = dbUtils.getByKey(key, Request.class);
		boolean duplicate = (null != req);
		if(!duplicate) {
			dbUtils.save(key, request);
		}
		dbUtils.closeDB();
		return duplicate;
	}

	@Override
	public void resetDuplicateCheck(Task task) {
		dbUtils.cleanAllData();
	}

	@Override
	public int getTotalRequestsCount(Task task) {
		List<Request> list = dbUtils.findAll(Request.class);
		if(null == list) {
			return 0;
		}
		return list.size();
	}
	
	public BerkeleyDBDuplicateFilter() {
		if(null != this.dbUtils) {
			return;
		}
		this.dbUtils = BerkeleyDBUtils.getInstance();
	}

	public BerkeleyDBUtils getDbUtils() {
		return dbUtils;
	}

	public void setDbUtils(BerkeleyDBUtils dbUtils) {
		this.dbUtils = dbUtils;
	}
}
