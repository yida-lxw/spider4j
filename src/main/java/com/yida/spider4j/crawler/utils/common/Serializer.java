package com.yida.spider4j.crawler.utils.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.yida.spider4j.crawler.core.Request;

/**
 * @ClassName: Serializer
 * @Description: Java序列化和反序列化
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月13日 上午10:23:11
 * 
 */
@SuppressWarnings("unchecked")
public class Serializer<T> {
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: serialize
	 * @Description: 对象序列化
	 * @param @param obj
	 * @param @return
	 * @return byte[]
	 * @throws
	 */
	public static byte[] serialize(Object obj) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = null;
		try {
			o = new ObjectOutputStream(b);
			o.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != o) {
				try {
					o.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return b.toByteArray();
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: deserialize
	 * @Description: 对象反序列化
	 * @param @param bytes
	 * @param @return
	 * @return T
	 * @throws
	 */
	public static <T> T deserialize(byte[] bytes) {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = null;
		try {
			o = new ObjectInputStream(b);
			return (T) o.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != o) {
				try {
					o.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static BigInteger checksum(Object obj) {
		if (obj == null) {
			return BigInteger.ZERO;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		MessageDigest m = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} finally {
			if (null != oos) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new BigInteger(1, m.digest());
	}

	public static String checksumHex(Object obj, int len) {
		BigInteger bigInteger = checksum(obj);
		if (null == bigInteger) {
			return "";
		}
		String hashtext = bigInteger.toString(16);
		while (hashtext.length() < len) {
			hashtext = "0" + hashtext;
		}
		return hashtext;
	}
	
	public static String checksumHex(Object obj) {
		return checksumHex(obj, 32);
	}

	public static void main(String[] args) {
		Request request1 = new Request();
		request1.setUrl("www.baidu.com");
		request1.setMethod("get");

		Request request2 = new Request();
		request2.setUrl("www.baidu.com");
		request2.setMethod("post");

		String s1 = checksumHex(request1,32).toString();
		String s2 = checksumHex(request2,32).toString();
		System.out.println(s1);
		System.out.println(s2);
	}
}
