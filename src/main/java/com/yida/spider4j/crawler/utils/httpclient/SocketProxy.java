package com.yida.spider4j.crawler.utils.httpclient;
/**
 * @ClassName: SocketProxy
 * @Description: Socket代理对象
 *               如：IP:50.63.67.54 端口：41928
 * @author Lanxiaowei
 * @date 2013-3-20 下午03:43:41
 */
public class SocketProxy {
	/**Socket代理主机IP*/
	private String socketHost;
	/**Socket代理端口号*/
	private int socketPort;
	/**是否Socket4代理*/
	private boolean socket4;
	public String getSocketHost() {
		return socketHost;
	}
	public void setSocketHost(String socketHost) {
		this.socketHost = socketHost;
	}
	public int getSocketPort() {
		return socketPort;
	}
	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}
	public boolean isSocket4() {
		return socket4;
	}
	public void setSocket4(boolean socket4) {
		this.socket4 = socket4;
	}
	
	public SocketProxy(String socketHost, int socketPort) {
		this.socketHost = socketHost;
		this.socketPort = socketPort;
	}
	public SocketProxy(String socketHost, int socketPort, boolean socket4) {
		this.socketHost = socketHost;
		this.socketPort = socketPort;
		this.socket4 = socket4;
	}
}
