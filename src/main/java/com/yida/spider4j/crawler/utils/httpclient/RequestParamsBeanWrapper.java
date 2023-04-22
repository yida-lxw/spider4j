package com.yida.spider4j.crawler.utils.httpclient;

import java.util.List;
import java.util.Map;

/**
 * Request请求参数包装器
 * @author Lanxiaowei
 */
public class RequestParamsBeanWrapper {
	/**请求URL*/
	private String url;
	/**请求头信息*/
	private Map<String,String> headers;
	/**普通表单数据*/
	private Map<String,String> params;
	/**文件域的表单元素名称*/
	private String fileFormName = "file";
	/**文件上传后的目标路径*/
	private List<String> fileParams;
	/**普通HTTP代理主机IP*/
	private String host;
	/**普通HTTP代理端口号*/
	private int port;
	/**Socket代理对象*/
	private SocketProxy socketProxy;
	/**请求默认编码*/
	private String encoding = "UTF-8";
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getFileFormName() {
		return fileFormName;
	}

	public void setFileFormName(String fileFormName) {
		this.fileFormName = fileFormName;
	}

	public List<String> getFileParams() {
		return fileParams;
	}

	public void setFileParams(List<String> fileParams) {
		this.fileParams = fileParams;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public SocketProxy getSocketProxy() {
		return socketProxy;
	}

	public void setSocketProxy(SocketProxy socketProxy) {
		this.socketProxy = socketProxy;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public RequestParamsBeanWrapper(){}

	public RequestParamsBeanWrapper(String url, Map<String, String> params) {
		this.url = url;
		this.params = params;
	}
}
