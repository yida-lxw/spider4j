package com.yida.spider4j.crawler.utils.charset.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.yida.spider4j.crawler.utils.charset.CharsetDetectorFacade;
import com.yida.spider4j.crawler.utils.io.FileUtils;

public class CharsetTest {
	public static void main(String[] args) throws IOException {
		InputStream is = FileUtils.file2InputStream(new File("C:/charset.txt"));
		String charset = CharsetDetectorFacade.getInstance()
			.detectCharset(is).name();
		System.out.println(charset);
	}
}
