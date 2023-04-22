package com.yida.spider4j.crawler.test.jsonpath;

import java.util.List;

import com.yida.spider4j.crawler.selector.jsonpath.JsonPathUtils;
import com.yida.spider4j.crawler.utils.io.FileUtils;

public class JsonPathTest2 {
	public static void main(String[] args) throws Exception {
		String json = FileUtils.readFile("C:/json.txt");
		String str = JsonPathUtils.selectOne(json, "$.appId");
		System.out.println(str);
	}
	
	private static void printList(List<String> authors) {
		if(authors == null || authors.size() <= 0) {
			System.out.println("Nothing.");
			return;
		}
		for(String author: authors) {
			System.out.println(author);
		}
	}
}
