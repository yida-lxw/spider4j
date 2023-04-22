package com.yida.spider4j.crawler.utils.proxy;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;

import com.yida.spider4j.crawler.utils.log.LogUtils;

/**
 * @ClassName: ProxyUtils
 * @Description: Http代理工具类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月29日 上午11:08:07
 *
 */
public class ProxyUtils {
	private static InetAddress localAddr;
	private static String networkInterface = "eth7";

	static {
		init();
	}

	private static void init() {
		try {
			//获取本机IP地址
			localAddr = InetAddress.getLocalHost();
			LogUtils.info("local IP:" + localAddr.getHostAddress());
		} catch (UnknownHostException e) {
			LogUtils.info("try again\n");
		}
		if (localAddr != null) {
			return;
		}
		// other way to get local IP
		Enumeration<InetAddress> localAddrs;
		try {
			// modify your network interface name
			NetworkInterface ni = NetworkInterface.getByName(networkInterface);
			if (ni == null) {
				return;
			}
			localAddrs = ni.getInetAddresses();
			if (localAddrs == null || !localAddrs.hasMoreElements()) {
				LogUtils.error("choose NetworkInterface\n" + getNetworkInterface());
				return;
			}
			while (localAddrs.hasMoreElements()) {
				InetAddress tmp = localAddrs.nextElement();
				if (!tmp.isLoopbackAddress() && !tmp.isLinkLocalAddress() && !(tmp instanceof Inet6Address)) {
					localAddr = tmp;
					LogUtils.info("local IP:" + localAddr.getHostAddress());
					break;
				}
			}
		} catch (Exception e) {
			LogUtils.error("Failure when init ProxyUtil:\n" + e.getMessage());
			LogUtils.error("choose NetworkInterface\n" + getNetworkInterface());
		}
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: validateProxy
	 * @Description: 验证代理是否可用
	 * @param @param p
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean validateProxy(HttpHost p) {
		if (localAddr == null) {
			LogUtils.error("cannot get local IP");
			return false;
		}
		boolean isReachable = false;
		Socket socket = null;
		try {
			socket = new Socket();
			socket.bind(new InetSocketAddress(localAddr, 0));
			InetSocketAddress endpointSocketAddr = new InetSocketAddress(p.getAddress().getHostAddress(), p.getPort());
			socket.connect(endpointSocketAddr, 3000);
			LogUtils.debug("SUCCESS - connection established! Local: " + localAddr.getHostAddress() + " remote: " + p);
			isReachable = true;
		} catch (IOException e) {
			LogUtils.warn("FAILRE - CAN not connect! Local: " + localAddr.getHostAddress() + " remote: " + p);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					LogUtils.warn("Error occurred while closing socket of validating proxy:\n" + e.getMessage());
				}
			}
		}
		return isReachable;
	}

	private static String getNetworkInterface() {
		String networkInterfaceName = ">>>> modify networkInterface in us.codecraft.webmagic.utils.ProxyUtils";
		Enumeration<NetworkInterface> enumeration = null;
		try {
			enumeration = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		while (enumeration.hasMoreElements()) {
			NetworkInterface networkInterface = enumeration.nextElement();

			Enumeration<InetAddress> addr = networkInterface.getInetAddresses();
			while (addr.hasMoreElements()) {
				String s = addr.nextElement().getHostAddress();
				Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
				if (s != null && IPV4_PATTERN.matcher(s).matches()) {
					networkInterfaceName += networkInterface.toString() + "IP:" + s + "\n\n";
				}
			}
		}
		return networkInterfaceName;
	}
}
