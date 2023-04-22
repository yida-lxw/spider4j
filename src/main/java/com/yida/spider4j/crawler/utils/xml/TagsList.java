package com.yida.spider4j.crawler.utils.xml;

import java.util.Arrays;
/**
 * @ClassName: TagsList
 * @Description: 存储提取出来的标签集合
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月17日 下午3:30:21
 *
 */
public class TagsList {
	private String[] data;
	private int size = 0;

	public TagsList(int size) {
		data = new String[size];
	}

	public TagsList() {
		this(10);
	}

	public void add(String str) {
		ensureCapacity(size + 1);
		data[size++] = str;
	}

	public String get(int index) {
		if (index < size)
			return data[index];
		else
			return null;
	}

	// 为了提高效率，只将其置为null
	public boolean remove(String str) {
		for (int index = size - 1; index >= 0; index--) {
			if (str.equals(data[index])) {
				data[index] = null;
				return true;
			}
		}
		return false;
	}

	public boolean remove(int index) {
		if (index < data.length) {
			data[index] = null;
			return true;
		}
		return false;
	}

	public int size() {
		return this.size;
	}

	// 扩展容量
	public void ensureCapacity(int minSize) {
		int oldCapacity = data.length;
		if (minSize > oldCapacity) {
			int newCapacity = (oldCapacity * 3 / 2 + 1) > minSize ? oldCapacity * 3 / 2 + 1
					: minSize;
			data = (String[]) Arrays.copyOf(data, newCapacity);
		}
	}
}
