package com.yida.spider4j.crawler.utils.httpclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * @ClassName: SocksSchemeSocketFactory
 * @Description: Socket套接字工厂
 * @author Lanxiaowei
 * @date 2013-3-20 下午03:15:08
 */
@SuppressWarnings("deprecation")
public class SocksSchemeSocketFactory implements SchemeSocketFactory {
	public Socket connectSocket(final Socket socket, final InetSocketAddress remoteAddress, 
		final InetSocketAddress localAddress, final HttpParams params) 
	    throws IOException, UnknownHostException, ConnectTimeoutException {
		if (remoteAddress == null) {
			throw new IllegalArgumentException("Remote address may not be null");
		}
		if (params == null) {
			throw new IllegalArgumentException("HTTP parameters may not be null");
		}
		Socket sock;
		if (socket != null) {
			sock = socket;
		} else {
			sock = createSocket(params);
		}
		if (localAddress != null) {
			sock.setReuseAddress(HttpConnectionParams.getSoReuseaddr(params));
			sock.bind(localAddress);
		}
		int timeout = HttpConnectionParams.getConnectionTimeout(params);
		try {
			sock.connect(remoteAddress, timeout);
		} catch (SocketTimeoutException ex) {
			throw new ConnectTimeoutException("Connect to " + remoteAddress.getHostName() + "/" + remoteAddress.getAddress() + " time out");
		}
		return sock;
	}

	public Socket createSocket(final HttpParams params) throws IOException {
		if (params == null) {
			throw new IllegalArgumentException("Http parameters may not be null");
		}
		String proxyHost = (String) params.getParameter("socks.host");
		Integer proxyPort = (Integer) params.getParameter("socks.port");
		InetSocketAddress socksAddr = new InetSocketAddress(proxyHost, proxyPort);
		Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksAddr);
		return new Socket(proxy);
	}

	public boolean isSecure(final Socket sock) throws IllegalArgumentException {
		return false;
	}
}
