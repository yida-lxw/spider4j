package com.yida.spider4j.crawler.pipeline;

import java.util.LinkedHashMap;
import java.util.Map;

import com.yida.spider4j.crawler.core.PageResultItem;
import com.yida.spider4j.crawler.task.Task;

/**
 * @ClassName: ConsolePipeline
 * @Description: 打印数据至控制台的管道
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 上午9:07:14
 *
 */
public class ConsolePipeline implements Pipeline {
	@Override
	public void process(PageResultItem pageResultItem, Task task) {
		if(pageResultItem == null) {
			System.out.println("result is empty.");
			return;
		}
		
		if((pageResultItem.getDataMap() == null || 
				pageResultItem.getDataMap().isEmpty()) && 
			(pageResultItem.getDataMapList() == null || 
						pageResultItem.getDataMapList().size() <= 0)) {
			System.out.println("result is empty.");
			return;
		}
		
		System.out.println("page url:" + pageResultItem.getRequest().getUrl());
		if(null != pageResultItem.getDataMap() && pageResultItem.getDataMap().size() > 0) {
			for (Map.Entry<String, Object> entry : pageResultItem.getAll().entrySet()) {
	            System.out.println(entry.getKey() + ":\t" + entry.getValue());
	        }
			System.out.println("\n");
		} else if(null != pageResultItem.getDataMapList() && pageResultItem.getDataMapList().size() > 0) {
			for(LinkedHashMap<String, Object> map : pageResultItem.getDataMapList()) {
				if(null == map || map.isEmpty()) {
					continue;
				}
				System.out.println("\n/*********************begin*******************/");
				for (Map.Entry<String, Object> entry : map.entrySet()) {
		            System.out.println(entry.getKey() + ":\t" + entry.getValue());
		        }
				System.out.println("/*********************end*******************/\n");
			}
			System.out.println("\n");
		} else {
			// do nothing at all.
		}
	}
}
