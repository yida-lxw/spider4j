package com.yida.spider4j.crawler.utils.httpclient;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import com.yida.spider4j.crawler.config.CrawlConfig;
import com.yida.spider4j.crawler.utils.HttpConstant;
import com.yida.spider4j.crawler.utils.common.GerneralUtils;
import com.yida.spider4j.crawler.utils.common.StringUtils;

/**
 * HttpClient4工具类
 * @author Lanxiaowei
 * @create 2013-01-25 15:42
 */
@SuppressWarnings({"deprecation","unused"})
public class HttpClientUtils {
	private static CrawlConfig config = CrawlConfig.getInstance();
	/**连接池最大链接数*/
    private static final int MAX_TOTAL_CONNECTION = config.getMaxTotalConnections();
    /**连接池每个路由最大链接并发数*/
    private static final int MAX_PRE_ROUTE = config.getMaxPreRoute();
    /**连接池HTTP请求80端口最大链接并发数*/
    private static final int MAX_HTTP_ROUTE = config.getMaxHttpRoute();
    /** 用户浏览器代理 */
	private static final String USER_AGENT = config.getUserAgent();
	/**默认请求编码UTF-8*/  
    private static final String HTTP_CONTENT_CHARSET = config.getHttpConnectCharset();
    /**Socket读取返回超时时间*/
	private static final int SO_TIMEOUT = config.getSocketTimeout();
    /**链接超时时间*/
    public static final int CONNECT_TIMEOUT = config.getConnectionTimeout();
    /**获取连接的最大等待时间 */  
    public static final int WAITING_TIMEOUT = config.getConnectionRequestTimeout();
    /**Socket工厂注册器*/
    private static SchemeRegistry schemeRegistry;
    /**连接池管理器*/
    private static ThreadSafeClientConnManager connectionManager;
    /**HTTP请求参数(单例)*/
    private static HttpParams httpParams;
    /**HttpClient实例对象(单例)*/
    private static DefaultHttpClient httpClient;
    
    static {
    	createConnectionManager();
    }
    
    /**
     * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复 
     */
    private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {  
        // 自定义的恢复策略  
        public boolean retryRequest(IOException exception, int executionCount,  
                HttpContext context) { 
            // 设置恢复策略，在发生异常时候将自动重试5次  
            if (executionCount >= 5) {  
                // 如果连接次数超过了最大值则停止重试  
                return false;  
            }  
            if (exception instanceof NoHttpResponseException) {  
                // 如果服务器连接失败重试  
                return true;  
            }
            if (exception instanceof SSLHandshakeException) {  
                //SSL连接异常 时不重试
                return false;  
            }  
            HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);  
            boolean idempotent = (request instanceof HttpEntityEnclosingRequest);  
            if (!idempotent) {  
                // 请求内容相同则重试 
                return true;  
            }  
            return false;  
        }  
    };  
    
    /** 
     * 重写验证方法，取消检测ssl 
     */  
    private static TrustManager truseAllManager = new X509TrustManager(){  
        public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {}  
        public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {}  
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;  
        }
    } ;  
	
	/** 
     * 支持访问https的网站 
     * @param httpClient 
     */  
	private static void enableSSL(DefaultHttpClient httpClient){  
        //调用SSL 
         try {  
            SSLContext sslcontext = SSLContext.getInstance("TLS");  
            sslcontext.init(null, new TrustManager[] { truseAllManager }, null);  
            SSLSocketFactory sf = new SSLSocketFactory(sslcontext);  
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
            Scheme https = new Scheme("https", sf, 443);  
            httpClient.getConnectionManager().getSchemeRegistry().register(https);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
	
	/**
	 * @Title: enableSocketProxy
	 * @Description: 启用Socket代理
	 * @param @param httpClient
	 * @return void
	 * @throws
	 */
	private static void enableSocketProxy(DefaultHttpClient httpClient) {
		httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("http", 80, new SocksSchemeSocketFactory())); 
	}
    
    /**
     * 设置HTTP请求链接参数
     */
	private static void setHttpParams(HttpParams httpParams,String charset) {
    	if(null == httpParams) {
    		return;
    	}
    	
    	//设置支持的HTTP协议版本
    	HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);  
    	//HTTP请求内容字符编码
        HttpProtocolParams.setContentCharset(httpParams, HTTP_CONTENT_CHARSET);  
    	// 设置连接超时时间  
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);  
        // 设置读取超时时间  
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
    	// 模拟浏览器，解决一些服务器程序只允许浏览器访问的问题  
    	httpParams.setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
    	httpParams.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);  
    	httpParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,charset == null ? HTTP_CONTENT_CHARSET : charset); 
    	//禁用陈旧链接检查(据API介绍,禁用此项能节省30ms)
    	httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, config.isStaleConnectionCheckEnabled());
    	//数据将会更早发送，增加了带宽消耗
    	httpParams.setParameter("http.tcp.nodelay", config.isSocketTcpNoDelay());
        //开启自动重定向
    	httpParams.setParameter(ClientPNames.HANDLE_REDIRECTS, config.isFollowRedirects());
        //Cookie自动设置策略(BROWSER_COMPATIBILITY:最大程度的兼容各种浏览器供应商的Cookie规范)
    	httpParams.setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.BROWSER_COMPATIBILITY);
    	httpParams.setParameter("http.protocol.single-cookie-header", config.isHttpProtocolSingleCookieHeader());   
        // 设置最大连接数  
        ConnManagerParams.setMaxTotalConnections(httpParams, MAX_TOTAL_CONNECTION);  
        /**从连接池中取连接的超时时间 */  
		ConnManagerParams.setTimeout(httpParams, config.getConnectionRequestTimeout());; 
        // 设置获取连接的最大等待时间  
        ConnManagerParams.setTimeout(httpParams, WAITING_TIMEOUT); 
        // 设置每个路由最大连接数  
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_PRE_ROUTE);  
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams,connPerRoute); 
    }
    
    /**
     * 设置HTTP请求链接参数
     */
    private static void setHttpParams(HttpParams httpParams) {
    	setHttpParams(httpParams,HTTP_CONTENT_CHARSET);
    }
    
    /**
     * Socket工厂注册
     * @return
     */
	private static SchemeRegistry getSchemeRegistry(){
		if(null == schemeRegistry) {
			schemeRegistry = new SchemeRegistry();
		}
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		return schemeRegistry;
	}
	
	/**
	 * 创建连接池
	 * @param schemeRegistry
	 * @return
	 */
	private static ThreadSafeClientConnManager createConnectionManager() {
		if(null == connectionManager) {
			connectionManager = new ThreadSafeClientConnManager(getSchemeRegistry());
		}
		
		connectionManager.setDefaultMaxPerRoute(MAX_PRE_ROUTE);
		HttpHost localhost = new HttpHost("locahost", 80);
		connectionManager.setMaxForRoute(new HttpRoute(localhost), MAX_HTTP_ROUTE);
		connectionManager.setMaxTotal(MAX_TOTAL_CONNECTION);
		return connectionManager;
	}
    
	/**
	 * @Title: getHttpClientInstance
	 * @Description: 单实例模式创建DefaultHttpClicent对象
	 * @param charset  请求编码
	 * @return DefaultHttpClient
	 * @throws
	 */
	public static DefaultHttpClient getHttpClientInstance(String charset){  
        if(null == httpClient) {  
            synchronized(HttpClient.class){  
            	if(null == httpClient) {  
            		httpClient = new DefaultHttpClient(connectionManager);
                }  
            }  
        }
        httpParams = httpClient.getParams();
    	setHttpParams(httpParams, charset);
    	//定义重试策略
    	//httpClient.setHttpRequestRetryHandler(requestRetryHandler);
        return httpClient;  
    }  
	
	/**
	 * @Title: getHttpClientInstance
	 * @Description: 单实例模式创建DefaultHttpClicent对象(重载)
	 *               默认请求编码为UTF-8
	 * @return DefaultHttpClient
	 * @throws
	 */
	public static DefaultHttpClient getHttpClientInstance() {
		return getHttpClientInstance(HTTP_CONTENT_CHARSET);
	}
	
    /**
     * 创建HttpClient实例
     * @param charset
     * @return
     */
    private static DefaultHttpClient getDefaultHttpClient(String charset) {
    	return getHttpClientInstance(charset);
    }
    
    /**
     * 创建HttpClient实例
     * @param charset
     * @return
     */
    private static DefaultHttpClient getDefaultHttpClient(){
    	return getDefaultHttpClient(HTTP_CONTENT_CHARSET);
    }
    
    /**
     * 发送get请求(支持Socket代理)
     * @param url             请求URL
     * @param headers         请求头信息
     * @param params          请求参数
     * @param socketProxy     Socket代理对象
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result get(String url,
    		Map<String,String> headers,Map<String,String> params,SocketProxy socketProxy) throws Exception {
    	DefaultHttpClient httpClient = getDefaultHttpClient();
		url = url + (null == params ? "" : assemblyParameter(params));
		HttpGet httpGet = new HttpGet(url);
		//判断是否是https请求  
        if(url.startsWith("https")){  
            enableSSL(httpClient);  
        }  
		//设置请求头信息
		if(null != headers){
			httpGet.setHeaders(assemblyHeader(headers));
		}
		//设置Socket代理
		if(null != socketProxy){
			enableSocketProxy(httpClient);
			httpClient.getParams().setParameter("socks.host", socketProxy.getSocketHost()); 
			httpClient.getParams().setParameter("socks.port", socketProxy.getSocketPort()); 
		}
		HttpResponse response = httpClient.execute(httpGet);
        Result result = produceResult(httpClient,response);
		//断开请求
		httpGet.abort();
		httpGet.releaseConnection();
		return result;
	}
    
    /**
     * 发送post请求
     * @param url      请求URL
     * @param headers  请求头信息
     * @param params   请求参数
     * @param host     代理主机IP
     * @param port     代理端口号
     * @param encoding 请求编码
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url,
    	Map<String,String> headers,Map<String,String> params,
    	String host,int port,String encoding) throws Exception {
    	DefaultHttpClient httpClient = getDefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		//判断是否是https请求  
        if(url.startsWith("https")){  
            enableSSL(httpClient);  
        }  
		//设置请求头信息
		if(null != headers){
			httpPost.setHeaders(assemblyHeader(headers));
		}
		//设置请求代理
		if(null != host && !"".equals(host) && port > 0){
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,new HttpHost(host,port));
		}
		//设置请求参数
		if(GerneralUtils.isNotEmptyMap(params)) {
			List<NameValuePair> list  = new ArrayList<NameValuePair>();
			for (String temp : params.keySet()) {
				list.add(new BasicNameValuePair(temp,params.get(temp)));
			}
			if(StringUtils.isEmpty(encoding)) {
				encoding = HttpConstant.Charset.UTF_8;
			}
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list,encoding);
			urlEncodedFormEntity.setChunked(false);
			httpPost.setEntity(urlEncodedFormEntity);
		}
		HttpResponse response = httpClient.execute(httpPost);
        Result result = produceResult(httpClient,response);
		httpPost.abort();
		httpPost.releaseConnection();
		return result ;
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: head
     * @Description: 发送Head请求
     * @param @param url
     * @param @param headers
     * @param @param host
     * @param @param port
     * @param @return
     * @param @throws Exception
     * @return Result
     * @throws
     */
    public static Result head(String url,
    	Map<String,String> headers,String host,int port) throws Exception {
    	DefaultHttpClient httpClient = getDefaultHttpClient();
    	HttpHead httpHead = new HttpHead(url);
    	//判断是否是https请求  
        if(url.startsWith("https")){  
            enableSSL(httpClient);  
        }
    	if(null != headers && !headers.isEmpty()) {
    		for(Map.Entry<String,String> entry : headers.entrySet()) {
    			String key = entry.getKey();
    			String value = entry.getValue();
    			httpHead.addHeader(key, value);
    		}
    	}
    	//设置请求代理
    	if(null != host && !"".equals(host) && port > 0){
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,new HttpHost(host,port));
		}
    	HttpResponse response = httpClient.execute(httpHead);
    	Result result = produceResult(httpClient,response);
    	httpHead.abort();
    	httpHead.releaseConnection();	
    	return result;
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: put
     * @Description: 发送PUT请求
     * @param @param url
     * @param @param headers
     * @param @param params
     * @param @param host
     * @param @param port
     * @param @param encoding
     * @param @return
     * @param @throws Exception
     * @return Result
     * @throws
     */
    public static Result put(String url,
    	Map<String,String> headers,Map<String,String> params,
    	String host,int port,String encoding) throws Exception {
    	DefaultHttpClient httpClient = getDefaultHttpClient();
    	HttpPut httpPut = new HttpPut(url);
		//判断是否是https请求  
        if(url.startsWith("https")){  
            enableSSL(httpClient);  
        }  
		//设置请求头信息
		if(null != headers){
			httpPut.setHeaders(assemblyHeader(headers));
		}
		//设置请求代理
		if(null != host && !"".equals(host) && port > 0){
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,new HttpHost(host,port));
		}
		//设置请求参数
		if(GerneralUtils.isNotEmptyMap(params)) {
			List<NameValuePair> list  = new ArrayList<NameValuePair>();
			for (String temp : params.keySet()) {
				list.add(new BasicNameValuePair(temp,params.get(temp)));
			}
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list,encoding);
			urlEncodedFormEntity.setChunked(false);
			httpPut.setEntity(urlEncodedFormEntity);
		}
		HttpResponse response = httpClient.execute(httpPut);
        Result result = produceResult(httpClient,response);
        httpPut.abort();
        httpPut.releaseConnection();
		return result ;
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: put
     * @Description: 发送PUT请求
     * @param @param url
     * @param @param headers
     * @param @param params
     * @param @param host
     * @param @param port
     * @param @param encoding
     * @param @param contentType
     * @param @return
     * @param @throws Exception
     * @return Result
     * @throws
     */
    public static Result put(String url,
    	Map<String,String> headers,String params,
    	String host,int port,String encoding,String contentType) throws Exception {
    	DefaultHttpClient httpClient = getDefaultHttpClient();
    	HttpPut httpPut = new HttpPut(url);
		//判断是否是https请求  
        if(url.startsWith("https")){  
            enableSSL(httpClient);  
        }  
		//设置请求头信息
		if(null != headers){
			httpPut.setHeaders(assemblyHeader(headers));
		}
		//设置请求代理
		if(null != host && !"".equals(host) && port > 0){
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,new HttpHost(host,port));
		}
		//设置请求参数
		if(StringUtils.isNotEmpty(params)) {
			if(StringUtils.isEmpty(encoding)) {
				encoding = HttpConstant.Charset.UTF_8;
			}
			StringEntity entity = new StringEntity(params,encoding);
			if(StringUtils.isNotEmpty(contentType)) {
				entity.setContentType(contentType);
			}
			httpPut.setEntity(entity);
		}
		HttpResponse response = httpClient.execute(httpPut);
        Result result = produceResult(httpClient,response);
        httpPut.abort();
        httpPut.releaseConnection();
		return result ;
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: put
     * @Description: 发送PUT请求[发送一个文件流]
     * @param @param url
     * @param @param headers
     * @param @param params
     * @param @param host
     * @param @param port
     * @param @param contentType
     * @param @return
     * @param @throws Exception
     * @return Result
     * @throws
     */
    public static Result put(String url,
    	Map<String,String> headers,InputStream params,
    	String host,int port,ContentType contentType) throws Exception {
    	DefaultHttpClient httpClient = getDefaultHttpClient();
    	HttpPut httpPut = new HttpPut(url);
		//判断是否是https请求  
        if(url.startsWith("https")){  
            enableSSL(httpClient);  
        }  
		//设置请求头信息
		if(null != headers){
			httpPut.setHeaders(assemblyHeader(headers));
		}
		//设置请求代理
		if(null != host && !"".equals(host) && port > 0){
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,new HttpHost(host,port));
		}
		//设置请求参数
		if(null != params) {
			InputStreamEntity entity = new InputStreamEntity(params,contentType);
			httpPut.setEntity(entity);
		}
		HttpResponse response = httpClient.execute(httpPut);
        Result result = produceResult(httpClient,response);
        httpPut.abort();
        httpPut.releaseConnection();
		return result ;
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: delete
     * @Description: 发送DELETE请求
     * @param @param url
     * @param @param headers
     * @param @param host
     * @param @param port
     * @param @param encoding
     * @param @return
     * @param @throws Exception
     * @return Result
     * @throws
     */
    public static Result delete(String url,
    	Map<String,String> headers,String host,int port,
    	String encoding) throws Exception {
    	DefaultHttpClient httpClient = getDefaultHttpClient();
    	HttpDelete httpDelete = new HttpDelete(url);
		//判断是否是https请求  
        if(url.startsWith("https")){  
            enableSSL(httpClient);  
        }  
		//设置请求头信息
		if(null != headers){
			httpDelete.setHeaders(assemblyHeader(headers));
		}
		//设置请求代理
		if(null != host && !"".equals(host) && port > 0){
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,new HttpHost(host,port));
		}
		HttpResponse response = httpClient.execute(httpDelete);
        Result result = produceResult(httpClient,response);
        httpDelete.abort();
        httpDelete.releaseConnection();
		return result ;
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: trace
     * @Description: 发送Trace请求
     * @param @param url
     * @param @param headers
     * @param @param host
     * @param @param port
     * @param @param encoding
     * @param @return
     * @param @throws Exception
     * @return Result
     * @throws
     */
    public static Result trace(String url,
    	Map<String,String> headers,String host,int port,
    	String encoding) throws Exception {
    	DefaultHttpClient httpClient = getDefaultHttpClient();
    	HttpTrace httpTrace = new HttpTrace(url);
		//判断是否是https请求  
        if(url.startsWith("https")){  
            enableSSL(httpClient);  
        }  
		//设置请求头信息
		if(null != headers){
			httpTrace.setHeaders(assemblyHeader(headers));
		}
		//设置请求代理
		if(null != host && !"".equals(host) && port > 0){
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,new HttpHost(host,port));
		}
		HttpResponse response = httpClient.execute(httpTrace);
        Result result = produceResult(httpClient,response);
        httpTrace.abort();
        httpTrace.releaseConnection();
		return result ;
    }
    
    /**
     * 发送get请求(重载1)
     * @param url      请求URL
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result get(String url) throws Exception{
    	return get(url, null, null,null,0);
    }
    
    /**
     * 发送get请求(重载2)
     * @param url      请求URL
     * @param params   请求参数
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result get(String url,Map<String,String> params) throws Exception{
    	return get(url, null, params,null,0);
    }
    
    /**
     * 发送get请求(重载3)
     * @param url      请求URL
     * @param headers  请求头信息
     * @param params   请求参数
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result get(String url,Map<String,String> headers,Map<String,String> params) throws Exception{
    	return get(url, headers, params,null,0);
    }
    
    /**
     * 发送get请求
     * @param url      请求URL
     * @param headers  请求头信息
     * @param params   请求参数
     * @param host     代理主机IP
     * @param port     代理端口号
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result get(String url,Map<String,String> headers,Map<String,String> params,String host,int port) throws Exception {
		DefaultHttpClient httpClient = getDefaultHttpClient();
		url = url + (null == params ? "" : assemblyParameter(params));
		HttpGet httpGet = new HttpGet(url);
		//判断是否是https请求  
        if(url.startsWith("https")){  
            enableSSL(httpClient);  
        }  
		//设置请求头信息
		if(null != headers){
			httpGet.setHeaders(assemblyHeader(headers));
		}
		//设置请求代理
		if(null != host && host.equals("")){
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,new HttpHost(host,port));
		}
		HttpResponse response = httpClient.execute(httpGet);
		BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
		
		Result result= new Result();
		//设置返回的cookie
		result.setCookie(assemblyCookie(httpClient.getCookieStore().getCookies()));
		//设置响应状态码
		result.setStatusCode(response.getStatusLine().getStatusCode());
		//设置响应头信息
		result.setHeaders(response.getAllHeaders());
		//设置响应体
		result.setHttpEntity(entity);
		//断开请求
		httpGet.abort();
		httpGet.releaseConnection();
		return result;
	}
    
    /**
     * 发送post请求(重载1)
     * @param url      请求URL
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url) throws Exception {
    	return post(url,null,null,null,0);
    }
    
    /**
     * 发送post请求(重载2)
     * @param url      请求URL
     * @param params   请求参数
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url,Map<String,String> params) throws Exception {
    	return post(url,null,params,null,0);
    }
    
    /**
     * 发送post请求(重载3)
     * @param url      请求URL
     * @param headers  请求头信息
     * @param params   请求参数
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url,Map<String,String> headers,Map<String,String> params) throws Exception {
    	return post(url,headers,params,null,0);
    }
    
    /**
     * 发送post请求(重载4)
     * @param url      请求URL
     * @param headers  请求头信息
     * @param params   请求参数
     * @param host     代理主机IP
     * @param port     代理端口号
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url,Map<String,String> headers,Map<String,String> params,String host,int port) throws Exception {
    	return post(url,headers,params,host,port,HTTP_CONTENT_CHARSET);
    }
    
    /**
     * 发送post请求
     * @param url      请求URL
     * @param headers  请求头信息
     * @param params   请求参数
     * @param host     代理主机IP
     * @param port     代理端口号
     * @param encoding 请求编码
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url,Map<String,String> headers,Map<String,String> params,SocketProxy socketProxy,String encoding) throws Exception {
    	DefaultHttpClient httpClient = getDefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		//判断是否是https请求  
        if(url.startsWith("https")){  
            enableSSL(httpClient);  
        }  
		//设置请求头信息
		if(null != headers){
			httpPost.setHeaders(assemblyHeader(headers));
		}
		//设置Socket代理
		if(null != socketProxy){
			enableSocketProxy(httpClient);
			httpClient.getParams().setParameter("socks.host", socketProxy.getSocketHost()); 
			httpClient.getParams().setParameter("socks.port", socketProxy.getSocketPort()); 
		}
		//设置请求参数
		if(GerneralUtils.isNotEmptyMap(params)) {
			List<NameValuePair> list  = new ArrayList<NameValuePair>();
			for (String temp : params.keySet()) {
				list.add(new BasicNameValuePair(temp,params.get(temp)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(list,encoding));
		}
		HttpResponse response = httpClient.execute(httpPost);
		BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
		
        Result result = new Result();
        //设置返回的cookie
		result.setCookie(assemblyCookie(httpClient.getCookieStore().getCookies()));
		//设置响应状态码
		result.setStatusCode(response.getStatusLine().getStatusCode());
		//设置响应头信息
		result.setHeaders(response.getAllHeaders());
		//设置响应体
		result.setHttpEntity(entity);
		httpPost.abort();
		httpPost.releaseConnection();
		return result ;
    }
    
    /**
     * 发送post请求
     * @param url      请求URL
     * @param headers  请求头信息
     * @param params   请求参数
     * @param host     代理主机IP
     * @param port     代理端口号
     * @param encoding 请求编码
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url,Map<String,String> headers,String payload,String contentType,SocketProxy socketProxy,String encoding) throws ClientProtocolException, IOException {
    	if(contentType == null || "".equals(contentType) ||
    		(!contentType.equalsIgnoreCase("application/json") &&
    		!contentType.equalsIgnoreCase("application/xml"))) {
    		throw new IllegalArgumentException("content-type must be application/json or application/xml");
    	}
    	DefaultHttpClient httpClient = getDefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		//判断是否是https请求  
        if(url.startsWith("https")){  
            enableSSL(httpClient);  
        }  
		//设置请求头信息
		if(null != headers){
			httpPost.setHeaders(assemblyHeader(headers));
		}
		//设置Socket代理
		if(null != socketProxy){
			enableSocketProxy(httpClient);
			httpClient.getParams().setParameter("socks.host", socketProxy.getSocketHost()); 
			httpClient.getParams().setParameter("socks.port", socketProxy.getSocketPort()); 
		}
		//设置请求参数
		if(null != payload && !payload.equals("")) {
			//StringEntity entity = new StringEntity(payload, contentType, encoding);
			ByteArrayEntity entity = new ByteArrayEntity(payload.getBytes(encoding));
			entity.setContentType(contentType);
	        httpPost.setEntity(entity);
		}
		HttpResponse response = httpClient.execute(httpPost);
		BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
		
        Result result = new Result();
        //设置返回的cookie
		result.setCookie(assemblyCookie(httpClient.getCookieStore().getCookies()));
		//设置响应状态码
		result.setStatusCode(response.getStatusLine().getStatusCode());
		//设置响应头信息
		result.setHeaders(response.getAllHeaders());
		//设置响应体
		result.setHttpEntity(entity);
		httpPost.abort();
		httpPost.releaseConnection();
		return result ;
    }
    
    public static String postAjax(String url,Map<String,String> headers,String payload,String contentType,SocketProxy socketProxy,String encoding) throws Exception {
    	Result result = post(url, headers, payload, contentType, socketProxy, encoding);
    	StringEntityHandler entityHandler = new StringEntityHandler();
		return entityHandler.handleEntity(result.getHttpEntity());
    }
    
    public static String postAjax(String url,Map<String,String> headers,String payload,String contentType,String encoding) throws Exception {
    	return postAjax(url, headers, payload, contentType, null, encoding);
    }
    
    public static String postAjax(String url,Map<String,String> headers,String payload,String contentType) throws Exception {
    	return postAjax(url, headers, payload, contentType, null, "UTF-8");
    }
    
    public static String postAjax(String url,Map<String,String> headers,String payload) throws Exception {
    	return postAjax(url, headers, payload, "application/json", null, "UTF-8");
    }
    
    public static String postAjax(String url,String payload) throws Exception {
    	return postAjax(url, null, payload, "application/json", null, "UTF-8");
    }
    
    /**
     * 发送post请求
     * @param url      请求URL
     * @param headers  请求头信息
     * @param params   请求参数
     * @param host     代理主机IP
     * @param port     代理端口号
     * @param encoding 请求编码
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url,Map<String,String> headers,Map<String,String> params,SocketProxy socketProxy) throws Exception {
    	return post(url, headers, params, socketProxy,HTTP_CONTENT_CHARSET);
    }
    
    /**
     * 发送POST请求并支持文件批量上传
     * @param requestParams    请求参数包装器
     * @return
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public static Result postWithFileUpload(RequestParamsBeanWrapper requestParams) throws Exception {
    	DefaultHttpClient httpClient = getDefaultHttpClient();
		//设置普通表单请求参数
		MultipartEntity reqEntity = new MultipartEntity();
		if(GerneralUtils.isNotEmptyMap(requestParams.getParams())) {
			for (String key : requestParams.getParams().keySet()) {
				reqEntity.addPart(key, new StringBody(requestParams.getParams().get(key), "text/plain", Charset.forName(requestParams.getEncoding()))); 
			}
		}
		//设置文件域参数
		if(GerneralUtils.isNotEmptyCollection(requestParams.getFileParams())) {
			for(String file : requestParams.getFileParams()) {  
	            reqEntity.addPart(requestParams.getFileFormName(), new FileBody(new File(file)));  
	        }  
		}
		HttpPost httpPost = new HttpPost(requestParams.getUrl());
		//判断是否是https请求  
        if(requestParams.getUrl().startsWith("https")){  
            enableSSL(httpClient);  
        }  
		//设置请求头信息
		if(null != requestParams.getHeaders()){
			httpPost.setHeaders(assemblyHeader(requestParams.getHeaders()));
		}
		//设置请求代理
		if(null != requestParams.getHost() && requestParams.getHost().equals("")){
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,new HttpHost(requestParams.getHost(),requestParams.getPort()));
		}
		httpPost.setEntity(reqEntity);  
        HttpResponse response = httpClient.execute(httpPost);
        BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
		
        Result result = new Result();
        //设置返回的cookie
		result.setCookie(assemblyCookie(httpClient.getCookieStore().getCookies()));
		//设置响应状态码
		result.setStatusCode(response.getStatusLine().getStatusCode());
		//设置响应头信息
		result.setHeaders(response.getAllHeaders());
		//设置响应体
		result.setHttpEntity(entity);
		httpPost.abort();
		httpPost.releaseConnection();
		return result;
    }
    
    /**
     * 组装请求头信息
     * @param headers
     * @return
     */
    public static Header[] assemblyHeader(Map<String,String> headers){
		Header[] allHeader= new BasicHeader[headers.size()];
		int i  = 0;
		for (String str :headers.keySet()) {
			allHeader[i] = new BasicHeader(str,headers.get(str));
			i++;
		}
		return allHeader;
	}
	
	/**
	 * 组装Cookie
	 * @param cookies
	 * @return
	 */
	public static String assemblyCookie(List<Cookie> cookies){
		StringBuffer buffer = new StringBuffer();
		for (Cookie cookie : cookies) {
			buffer.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
		}
		if(buffer.length()>0){
			buffer.deleteCharAt(buffer.length()-1);
		}
		return buffer.toString();
	}
	
	/**
	 * 组装请求参数
	 * @param parameters
	 * @return
	 */
	public static String assemblyParameter(Map<String,String> parameters){
		String para = "?";
		for (String str :parameters.keySet()) {
			String val = parameters.get(str);
			if(StringUtils.containsChinese(val)) {
				val = URLEncoder.encode(val);
			}
			para += str + "=" + val + "&";
		}
		return para.substring(0,para.length() - 1);
	}
	
	/**
	 * 获取HTML内容(默认get方式)
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHTML(String url) throws Exception {
		return getHTML(url,null,true);
	}
	
	/**
	 * 获取HTML内容(默认get方式)
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHTML(String url,Map<String,String> headers) throws Exception {
		return getHTML(url,headers,true);
	}
	
	/**
	 * @Title: getHTML
	 * @Description: 获取HTML内容(支持Socket代理)
	 * @param @param url
	 * @param @param headers
	 * @param @param socketProxy
	 * @param @param isGet
	 * @param @return
	 * @param @throws ClientProtocolException
	 * @param @throws IOException
	 * @return String
	 * @throws
	 */
	public static String getHTML(String url,Map<String,String> headers,SocketProxy socketProxy,boolean isGet) throws Exception {
		Result result = null;
		if(isGet) {
			result = get(url, headers, null, socketProxy);
		} else {
			result = post(url, headers, null, socketProxy);
		}	
		StringEntityHandler entityHandler = new StringEntityHandler();
		return entityHandler.handleEntity(result.getHttpEntity());
	}
	
	/**
	 * @Title: getHTML
	 * @Description: 获取HTML内容(支持Socket代理,默认get方式)
	 * @param @param url
	 * @param @param headers
	 * @param @param socketProxy
	 * @param @return
	 * @param @throws ClientProtocolException
	 * @param @throws IOException
	 * @return String
	 * @throws
	 */
	public static String getHTML(String url,Map<String,String> headers,SocketProxy socketProxy) throws Exception {
		return getHTML(url,headers,socketProxy,true);
	}
	
	/**
	 * 获取HTML内容(默认get方式)
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHTML(String url,Map<String,String> headers,String host,int port,boolean isGet) throws Exception {
		Result result = null;
		if(isGet) {
			result = get(url, headers, null, host, port);
		} else {
			result = post(url, headers, null, host, port);
		}	
		StringEntityHandler entityHandler = new StringEntityHandler();
		return entityHandler.handleEntity(result.getHttpEntity());
	}
	
	/**
	 * 获取HTML内容(默认get方式)
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHTML(String url,Map<String,String> headers,String host,int port) throws Exception {
		return getHTML(url, headers, host, port,true);
	}
	
	/**
	 * 获取HTML内容(默认get方式)
	 * @param url     请求地址
	 * @param host    代理服务器主机IP
	 * @param port    端口号
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHTML(String url,String host,int port,boolean isGet) throws Exception {
		Result result = null;
		if(isGet) {
			result = get(url, null, null, host, port);
		} else {
			result = post(url, null, null, host, port);
		}	
		StringEntityHandler entityHandler = new StringEntityHandler();
		return entityHandler.handleEntity(result.getHttpEntity());
	}
	
	/**
	 * 获取HTML内容(默认get方式)
	 * @param url     请求地址
	 * @param host    代理服务器主机IP
	 * @param port    端口号
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHTML(String url,String host,int port) throws Exception {
		return getHTML(url, host, port, true);
	}
	
	
	/**
	 * 获取HTML内容
	 * @param url
	 * @param isGet  get/post
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHTML(String url,Map<String,String> headers,boolean isGet) throws Exception {
		Result result = null;
		if(isGet) {
			result = get(url,headers,null);
		} else {
			result = post(url,headers,null);
		}	
		StringEntityHandler entityHandler = new StringEntityHandler();
		return entityHandler.handleEntity(result.getHttpEntity());
	}
	
	/**
	 * 获取HTML内容
	 * @param url
	 * @param isGet  get/post
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHTML(String url,Map<String,String> headers,Map<String,String> params,boolean isGet) throws Exception{
		Result result = null;
		if(isGet) {
			result = get(url,headers,params);
		} else {
			result = post(url,headers,params);
		}	
		StringEntityHandler entityHandler = new StringEntityHandler();
		return entityHandler.handleEntity(result.getHttpEntity());
	}
	
	/**
	 * 获取HTML内容
	 * @param url
	 * @param isGet  get/post
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHTML(String url,Map<String,String> headers,Map<String,String> params) throws Exception{
		return getHTML(url,headers,params,true);
	}
	
	/**
	 * 获取HTML内容(添加自动重连机制)
	 * @param url
	 * @param headers
	 * @param isGet
	 * @param retryCount
	 * @return
	 */
	public static String getHTMLWithRetry(String url,Map<String,String> headers,boolean isGet,int retryCount){
    	return getHTMLWithRetry(url, null, headers, isGet, retryCount);
    }
	
	public static String getHTMLWithRetry(String url,Map<String,String> params,Map<String,String> headers,boolean isGet,int retryCount){
    	boolean flag = true;
    	String html = null;
    	int count = 0;
    	while(flag) {
    		try {
    			//超过最大重连次数则停止
    			if(count > retryCount) {
    				break;
    			}
				html = getHTML(url,headers,params,isGet);
			} catch (Exception e) {
				count++;
				continue;
			}

			if(GerneralUtils.isNotEmptyString(html)) {
				/*String is404 = null;
				if(html.toLowerCase().indexOf("<title>") != -1) {
					is404 = html.substring(html.toLowerCase().indexOf("<title>") + 7,html.toLowerCase().indexOf("</title>"));
				}*/
				if(!html.startsWith("<html><body><h1>Request Time-out</h1>") && html.indexOf("<title>Error</title>") == -1) {
					flag = false;
					count = 0;
				} else {
					html = null;
					count++;
					continue;
				}
			} else {
				count++;
			}
    	}
    	return html;
    }
	
	/**
	 * 获取HTML内容(添加自动重连机制)
	 * @param url
	 * @param headers
	 * @param isGet
	 * @param retryCount
	 * @return
	 */
	public static String getHTMLWithRetry(String url,Map<String,String> headers,int retryCount) {
		return getHTMLWithRetry(url, headers, true, retryCount);
	}
	
	/**
	 * 获取HTML内容(添加自动重连机制)
	 * 不指定重试次数，默认自动重试15次
	 * @param url
	 * @param headers
	 * @param isGet
	 * @param retryCount
	 * @return
	 */
	public static String getHTMLWithRetry(String url,Map<String,String> headers) {
		return getHTMLWithRetry(url, headers, true, 15);
	}
	
	/**
	 * 获取HTML内容(添加自动重连机制)
	 * @param url
	 * @param headers
	 * @param isGet
	 * @param retryCount
	 * @return
	 */
	public static String getHTMLWithRetry(String url,int retryCount){
		return getHTMLWithRetry(url, null, true, retryCount);
	}
	
	/**
	 * 获取HTML内容(添加自动重连机制)
	 * 不指定重试次数，默认自动重试15次
	 * @param url
	 * @param headers
	 * @param isGet
	 * @param retryCount
	 * @return
	 */
	public static String getHTMLWithRetry(String url) {
		return getHTMLWithRetry(url, null, true, 15);
	}
	
	/**
	 * 获取字节数组
	 * (若采用GET方式且请求参数未指定,请自行把请求参数拼接至URL后面,如：http://www.xxxx.com?id=xx&page=xx)
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param params    请求参数
	 * @param isGet     GET/POST
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static byte[] getByteArray(String url,Map<String,String> headers,Map<String,String> params,boolean isGet)throws Exception {
		Result result = null;
		if(isGet) {
			result = get(url,headers,params);
		} else {
			result = post(url,headers,params);
		}	
		ByteArrayEntityHandler entityHandler = new ByteArrayEntityHandler();
		return entityHandler.handleEntity(result.getHttpEntity());
	}
	
	/**
	 * 获取字节数组
	 * (默认采用GET方式发送HTTP请求)
	 * (若采用GET方式且请求参数未指定,请自行把请求参数拼接至URL后面,如：http://www.xxxx.com?id=xx&page=xx)
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param params    请求参数
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static byte[] getByteArray(String url,Map<String,String> headers,Map<String,String> params)throws Exception {
		return getByteArray(url, headers, params, true);
	}
	
	/**
	 * 获取字节数组
	 * (默认采用GET方式发送HTTP请求)
	 * (若采用GET方式且请求参数未指定,请自行把请求参数拼接至URL后面,如：http://www.xxxx.com?id=xx&page=xx)
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param params    请求参数
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static byte[] getByteArray(String url,Map<String,String> headers)throws Exception {
		return getByteArray(url, headers, null, true);
	}
	
	/**
	 * 获取字节数组
	 * (默认采用GET方式发送HTTP请求)
	 * (若采用GET方式且请求参数未指定,请自行把请求参数拼接至URL后面,如：http://www.xxxx.com?id=xx&page=xx)
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param params    请求参数
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static byte[] getByteArray(String url)throws Exception {
		return getByteArray(url, null, null, true);
	}
	
	/**
	 * 获取输入流
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param params    请求参数
	 * @param isGet     GET/POST
	 * @param isRange   是否开启多线程分段下载模式
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static InputStream getInputStream(String url,Map<String,String> headers,Map<String,String> params,boolean isGet,boolean isRange) throws Exception {
		Result result = null;
		if(isGet) {
			result = get(url,headers,params);
		} else {
			result = post(url,headers,params);
		}	
		InputStreamEntityHandler entityHandler = new InputStreamEntityHandler(isRange,result.getStatusCode());
		return entityHandler.handleEntity(result.getHttpEntity());
	}
	
	/**
	 * 获取输入流
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param params    请求参数
	 * @param isGet     GET/POST
	 * @param isRange   是否开启多线程分段下载模式
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static InputStream getInputStream(String url,Map<String,String> headers,Map<String,String> params,boolean isRange) throws Exception {
		return getInputStream(url, headers, params, true, isRange);
	}
	
	/**
	 * 获取输入流
	 * (默认GET方式、默认多线程分段下载模式关闭)
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param params    请求参数
	 * @param isGet     GET/POST
	 * @param isRange   是否开启多线程分段下载模式
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static InputStream getInputStream(String url,Map<String,String> headers,Map<String,String> params) throws Exception {
		return getInputStream(url, headers, params, true, false);
	}
	
	/**
	 * 获取输入流
	 * (默认GET方式、默认多线程分段下载模式关闭)
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param isGet     GET/POST
	 * @param isRange   是否开启多线程分段下载模式
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static InputStream getInputStream(String url,Map<String,String> headers) throws Exception{
		return getInputStream(url, headers, null, true, false);
	}
	
	/**
	 * 获取输入流
	 * (默认GET方式)
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param isGet     GET/POST
	 * @param isRange   是否开启多线程分段下载模式
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static InputStream getInputStream(String url,Map<String,String> headers,boolean isRange) throws Exception{
		return getInputStream(url, headers, null, true, isRange);
	}
	
	/**
	 * 获取输入流
	 * (默认GET方式、默认多线程分段下载模式关闭)
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param isGet     GET/POST
	 * @param isRange   是否开启多线程分段下载模式
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static InputStream getInputStream(String url) throws Exception {
		return getInputStream(url, null, null, true, false);
	}
	
	/**
	 * 获取输入流
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param isGet     GET/POST
	 * @param isRange   是否开启多线程分段下载模式
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static InputStream getInputStream(String url,boolean isGet,boolean isRange) throws Exception {
		return getInputStream(url, null, null, isGet, isRange);
	}
	
	/**
	 * 获取输入流
	 * (默认GET方式)
	 * @param url       请求URL
	 * @param headers   请求头信息
	 * @param isGet     GET/POST
	 * @param isRange   是否开启多线程分段下载模式
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static InputStream getInputStream(String url,boolean isRange) throws Exception {
		return getInputStream(url, null, null, true, isRange);
	}
	
	/**
	 * 下载文件(单线程下载，适用于几M的小文件)
	 * @param url       待下载文件链接
	 * @param headers   请求头信息  
	 * @param savePath  保存路径(不指定，默认设置为C:/temp/DOWNLOAD)
	 * @param fileName  下载下来的文件保存的文件名称(不指定，默认设置为New-File.temp)
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static File downloadFile(String url,Map<String,String> headers,String savePath,String fileName) throws Exception {
		Result result = get(url, headers, null);
		FileEntityHandler entityHandler = new FileEntityHandler(savePath, fileName);
		return entityHandler.handleEntity(result.getHttpEntity());
	}
	
	/**
	 * 下载文件(单线程下载，适用于几M的小文件)
	 * (若用户未自定义下载文件名称，则默认设置为New-File.temp)
	 * @param url       待下载文件链接
	 * @param headers   请求头信息  
	 * @param savePath  保存路径(不指定，默认设置为C:/temp/DOWNLOAD)
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static File downloadFile(String url,Map<String,String> headers,String savePath) throws Exception{
		return downloadFile(url, headers, savePath, null);
	}
	
	/**
	 * 下载文件(单线程下载，适用于几M的小文件)
	 * (若用户未自定义下载文件名称，则默认设置为New-File.temp)
	 * (若用户未自定义下载保存目录，则默认设置为C:/temp/DOWNLOAD)
	 * @param url       待下载文件链接
	 * @param headers   请求头信息
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static File downloadFile(String url,Map<String,String> headers) throws Exception {
		return downloadFile(url, headers, null, null);
	}
	
	/**
	 * 下载文件(单线程下载，适用于几M的小文件)
	 * (若用户未自定义下载文件名称，则默认设置为New-File.temp)
	 * (若用户未自定义下载保存目录，则默认设置为C:/temp/DOWNLOAD)
	 * @param url       待下载文件链接
	 * @param headers   请求头信息
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static File downloadFile(String url) throws Exception {
		return downloadFile(url, null, null, null);
	}
	
	/**
	 * 获取响应体
	 * @param url
	 * @param headers
	 * @return
	 */
	public static Object[] getResponse(String url,Map<String,String> headers) {
		DefaultHttpClient httpClient = getDefaultHttpClient();
		HttpHead httpHead = new HttpHead(url);
		//设置请求头信息
		if(GerneralUtils.isNotEmptyMap(headers)) {
			for(Map.Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey();
				String val = entry.getValue();
				httpHead.addHeader(key, val);
			}
		}
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpHead);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//获取HTTP响应状态码
		int statusCode = response.getStatusLine().getStatusCode();
		Object[] result = null;
		if(!(statusCode >= 200 && statusCode <= 399)) {
			result = new Object[] {statusCode,null};
		} else {
			Header[] headerArray = response.getAllHeaders();
			result = new Object[] {statusCode,headerArray};
		}
		httpHead.abort();
		httpHead.releaseConnection();
		return result;
	}
	
	/**
	 * 获取响应体
	 * @param url
	 * @param headers
	 * @return
	 */
	public static Object[] getResponse(String url) {
		return getResponse(url, null);
	}
	
	/**
	 * 获取Content-Disposition响应头
	 * @param url
	 * @return
	 */
	public static String getContentDisposition(String url) {
		DefaultHttpClient httpClient = getDefaultHttpClient();
		HttpHead httpHead = new HttpHead(url);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpHead);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String contentDisposition = null;
		if(response.getStatusLine().getStatusCode() == 200){
			Header[] headers = response.getHeaders("Content-Disposition");
			if(headers.length > 0) {
				contentDisposition = headers[0].getValue();
			}
		}
		httpHead.abort();
		httpHead.releaseConnection();
		return contentDisposition;
	}
	
	/**
	 * 获取响应头信息，如Content-Disposition、Content-Length
	 * @param url  请求URL
	 * @headers    设置请求头信息
	 * @return
	 */
	public static Header[] getResponseHeaders(String url,Map<String,String> headers) {
		DefaultHttpClient httpClient = getDefaultHttpClient();
		HttpHead httpHead = new HttpHead(url);
		//设置请求头信息
		if(GerneralUtils.isNotEmptyMap(headers)) {
			for(Map.Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey();
				String val = entry.getValue();
				httpHead.addHeader(key, val);
			}
		}
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpHead);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//获取HTTP响应状态码
		int statusCode = response.getStatusLine().getStatusCode();
		if(!(statusCode >= 200 && statusCode <= 399)) {
			return null;
		}
		Header[] headerArray = response.getAllHeaders();
		httpHead.abort();
		httpHead.releaseConnection();
		return headerArray;
	}
	
	/**
	 * 获取响应头信息，如Content-Disposition、Content-Length
	 * @param url  请求URL
	 * @return
	 */
	public static Header[] getResponseHeaders(String url) {
		return getResponseHeaders(url, null);
	}
	
	/**
	 * 获取响应状态码
	 * @param url  请求URL
	 * @headers    设置请求头信息
	 * @return
	 */
	public static int getStatusCode(String url,Map<String,String> headers) {
		DefaultHttpClient httpClient = getDefaultHttpClient();
		HttpHead httpHead = new HttpHead(url);
		//设置请求头信息
		if(GerneralUtils.isNotEmptyMap(headers)) {
			for(Map.Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey();
				String val = entry.getValue();
				httpHead.addHeader(key, val);
			}
		}
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpHead);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//获取HTTP响应状态码
		int statusCode = response.getStatusLine().getStatusCode();
		httpHead.abort();
		httpHead.releaseConnection();
		return statusCode;
	}
	
	/**
	 * 获取响应状态码(用于判断某资源是否存在)
	 * @param url  请求URL
	 * @return
	 */
	public static int getStatusCode(String url) {
		return getStatusCode(url, null);
	}
	
	/**
	 * @Title: getLocation
	 * @Description: 获取重定向后的链接URL
	 * @param @param url
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getLocation(String url) {
		Header[] headers = getResponseHeaders(url);
		return HttpClientUtils.getHeaderByKey(headers, "Location");
	}
	
	/**
	 * 根据key获取指定的请求/响应头信息
	 * @param headers
	 * @param key
	 * @return
	 */
	public static String getHeaderByKey(Header[] headers,String key) {
		if(GerneralUtils.isEmptyArray(headers) || GerneralUtils.isEmptyString(key)) {
			return null;
		}
		for (Header header : headers) {
			if(header.getName().equals(key)) {
				return header.getValue();
			}
		}
		return "";
	}
	
	/**
	 * 根据key获取请求/响应头信息
	 * @param key
	 * @return
	 */
	public static String getHeaderByKey(Map<String,String> headerMap,String key){
		if (headerMap == null || headerMap.isEmpty()) {
			return null;
		}
		Iterator<Map.Entry<String, String>> iter = headerMap.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry<String, String> entry = iter.next(); 
		    String ky = entry.getKey();
		    if(ky.equals(key)){
		    	return entry.getValue().toString();
		    }
		} 
		return "";
	}
	
	/**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: produceResult
     * @Description: 生产Result
     * @param @param httpClient
     * @param @param response
     * @param @return
     * @param @throws Exception
     * @return Result
     * @throws
     */
    private static Result produceResult(DefaultHttpClient httpClient,
    	HttpResponse response) throws Exception {
    	BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
    	Result result = new Result();
    	//设置返回的cookie
    	result.setCookie(assemblyCookie(httpClient.getCookieStore().getCookies()));
		//设置响应状态码
		result.setStatusCode(response.getStatusLine().getStatusCode());
		//设置响应头信息
		result.setHeaders(response.getAllHeaders());
		//设置响应体
		result.setHttpEntity(entity);
		return result;
    }
} 