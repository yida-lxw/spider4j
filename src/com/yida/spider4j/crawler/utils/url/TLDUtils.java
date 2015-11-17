package com.yida.spider4j.crawler.utils.url;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.yida.spider4j.crawler.utils.log.LogUtils;
/**
 * @ClassName: TLDUtils
 * @Description: 顶级域名处理工具类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月29日 下午5:17:45
 *
 */
public class TLDUtils {
	private static final String TLD_NAMES_ONLINE_URL = "https://publicsuffix.org/list/effective_tld_names.dat";
	private static final String TLD_NAMES_TXT_FILENAME = "tld-names.txt";

	private static boolean onlineUpdate = false;
	private final Set<String> tldSet = new HashSet<String>(10000);

	private static final TLDUtils instance = new TLDUtils();

	public static TLDUtils getInstance() {
		return instance;
	}

	private TLDUtils() {
		if (onlineUpdate) {
			URL url;
			try {
				url = new URL(TLD_NAMES_ONLINE_URL);
			} catch (MalformedURLException e) {
				// This cannot happen... No need to treat it
				LogUtils.error("Invalid URL: {" + TLD_NAMES_ONLINE_URL + "}");
				throw new RuntimeException(e);
			}

			try {
				InputStream stream = url.openStream();
				LogUtils.debug("Fetching the most updated TLD list online");
				int n = readStream(stream);
				LogUtils.info("Obtained {" + n + "} TLD from URL {"
						+ TLD_NAMES_ONLINE_URL + "}");
				return;
			} catch (Exception e) {
				LogUtils.error("Couldn't fetch the online list of TLDs from: {"
						+ TLD_NAMES_ONLINE_URL + "}:\n" + e.getMessage());
			}
		}

		File f = new File(TLD_NAMES_TXT_FILENAME);
		if (f.exists()) {
			LogUtils.debug("Fetching the list from a local file {"
					+ TLD_NAMES_TXT_FILENAME + "}");
			try {
				InputStream tldFile = new FileInputStream(f);
				int n = readStream(tldFile);
				LogUtils.info("Obtained {" + n + "} TLD from local file {"
						+ TLD_NAMES_TXT_FILENAME + "}");
				return;
			} catch (IOException e) {
				LogUtils.error("Couldn't read the TLD list from local file:\n"
						+ e.getMessage());
			}
		}
		InputStream tldFile = getClass().getClassLoader()
				.getResourceAsStream(TLD_NAMES_TXT_FILENAME);
		int n = readStream(tldFile);
		LogUtils.info("Obtained {" + n + "} TLD from packaged file {"
				+ TLD_NAMES_TXT_FILENAME + "}");
	}

	private int readStream(InputStream stream) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("//")) {
					continue;
				}
				tldSet.add(line);
			}
		} catch (IOException e) {
			LogUtils.warn("Error while reading TLD-list:\n" + e.getMessage());
		}
		return tldSet.size();
	}

	/** 是否在线更新顶级域名列表,否则会加载本地文件里存储的顶级域名 */
	public static void setUseOnline(boolean online) {
		onlineUpdate = online;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: contains
	 * @Description: 判断是否包含指定域名
	 * @param @param str
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean contains(String str) {
		return tldSet.contains(str);
	}
}
