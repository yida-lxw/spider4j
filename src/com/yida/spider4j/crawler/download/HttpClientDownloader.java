package com.yida.spider4j.crawler.download;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.core.Request;
import com.yida.spider4j.crawler.core.Site;
import com.yida.spider4j.crawler.selector.PlainText;
import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.utils.HttpConstant;
import com.yida.spider4j.crawler.utils.charset.CharsetDetectorFacade;
import com.yida.spider4j.crawler.utils.common.GerneralUtils;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.httpclient.HttpClientUtils;
import com.yida.spider4j.crawler.utils.httpclient.Result;
import com.yida.spider4j.crawler.utils.httpclient.SocketProxy;
import com.yida.spider4j.crawler.utils.httpclient.SocksSchemeSocketFactory;
import com.yida.spider4j.crawler.utils.httpclient.StringEntityHandler;
import com.yida.spider4j.crawler.utils.log.LogUtils;


/**
 * @ClassName: HttpClientDownloader
 * @Description: 基于HttpClient4.x实现的网页下载器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月10日 下午6:34:38
 *
 */
@ThreadSafe
@SuppressWarnings({"deprecation","unused"})
public class HttpClientDownloader extends AbstractDownloader {

    private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    private CloseableHttpClient getHttpClient(Site site) {
        if (site == null) {
            return httpClientGenerator.getClient(site);
        }
        String domain = site.getDomain();
        CloseableHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(site);
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    @Override
    public Page download(Request request, Task task) {
        Site site = null;
        if (task != null) {
            site = task.getSite();
        }
        Set<Integer> acceptStatCode;
        String charset = null;
        if (site != null) {
            acceptStatCode = site.getAcceptStatCode();
            charset = site.getCharset();
            if(null == request.getHeaders() || request.getHeaders().isEmpty()) {
            	request.setHeaders(site.getHeaders());
            }
        } else {
            acceptStatCode = new HashSet<Integer>();
            acceptStatCode.add(200);
        }
        LogUtils.info("begin downloading page {" + request.getUrl() + "}");
        
        Result result = null;
        try {
        	//根据Request对象获取响应结果
        	result = buildResult(request, site);
            
            //获取响应状态码
            int statusCode = result.getStatusCode();
            request.putExtra(Request.STATUS_CODE, statusCode);
            //若返回的响应状态码在指定范围内,则提取网页内容
            if (statusAccept(acceptStatCode, statusCode)) {
                Page page = handleResponse(request, charset, result);
                onSuccess(request);
                return page;
            }
            LogUtils.warn("code error " + statusCode + "\t" + request.getUrl());
            //FileUtils.writeFile(request.getUrl() + "\n", "C:/movie.txt", "UTF-8", true);
            return null;
        } catch (IOException e) {
        	LogUtils.warn("download page " + request.getUrl() + " occur IOException:\n" + e.getMessage());
        	//获取网页下载失败的重试次数
            if (site.getCycleRetryTimes() > 0) {
                return addToCycleRetry(request, site);
            }
            onError(request);
            //FileUtils.writeFile(request.getUrl() + "\n", "C:/movie.txt", "UTF-8", true);
            return null;
        } catch (Exception e) {
        	LogUtils.warn("download page " + request.getUrl() + " occur error:\n" + e.getMessage());
        	//获取网页下载失败的重试次数
            if (site.getCycleRetryTimes() > 0) {
                return addToCycleRetry(request, site);
            }
            onError(request);
            return null;
		} finally {
        	request.putExtra(Request.STATUS_CODE, result.getStatusCode());
            try {
                if (result.getHttpEntity() != null) {
                    // 关闭输入流,释放资源
                    EntityUtils.consume(result.getHttpEntity());
                }
            } catch (IOException e) {
            	LogUtils.warn("close response fail:\n" + e.getMessage());
            }
        }
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: buildResult
     * @Description: 获取请求的响应结果
     * @param @param request
     * @param @param site
     * @param @return
     * @param @throws Exception
     * @return Result
     * @throws
     */
    private Result buildResult(Request request,Site site) throws Exception {
    	Result result = null;
    	String method = request.getMethod();
    	String url = request.getUrl();
        if (StringUtils.isEmpty(method) || 
        	method.equalsIgnoreCase(HttpConstant.Method.GET)) {
        	result = HttpClientUtils.get(url, request.getHeaders(), 
        		request.getParams(), buildSocketProxy(site, request));
        } else if (method.equalsIgnoreCase(HttpConstant.Method.POST)) {
        	SocketProxy proxy = buildSocketProxy(site, request);
        	if(null == proxy) {
        		result = HttpClientUtils.post(url, request.getHeaders(), 
                	request.getParams(), null,-1, 
                	request.getRequestBodyEncoding());
        	} else {
        		result = HttpClientUtils.post(url, request.getHeaders(), 
                	request.getParams(), proxy.getSocketHost(), 
                	proxy.getSocketPort(), request.getRequestBodyEncoding());
        	}
        } else if (method.equalsIgnoreCase(HttpConstant.Method.HEAD)) {
        	SocketProxy proxy = buildSocketProxy(site, request);
        	if(null == proxy) {
        		result = HttpClientUtils.head(url, request.getHeaders(),
        			null, -1);
        	} else {
        		result = HttpClientUtils.head(url, request.getHeaders(),
            		proxy.getSocketHost(), proxy.getSocketPort());
        	}
        } else if (method.equalsIgnoreCase(HttpConstant.Method.PUT)) {
        	SocketProxy proxy = buildSocketProxy(site, request);
        	if(null == proxy) {
        		result = HttpClientUtils.put(url, request.getHeaders(), 
                	request.getParams(), null,-1, 
                	request.getRequestBodyEncoding());
        	} else {
        		result = HttpClientUtils.put(url, request.getHeaders(), 
                	request.getParams(), proxy.getSocketHost(), 
                	proxy.getSocketPort(), request.getRequestBodyEncoding());
        	}
        } else if (method.equalsIgnoreCase(HttpConstant.Method.DELETE)) {
        	SocketProxy proxy = buildSocketProxy(site, request);
        	if(null == proxy) {
        		result = HttpClientUtils.delete(url,request.getHeaders(),null,-1,
        			request.getRequestBodyEncoding());
        	} else {
        		result = HttpClientUtils.delete(url, request.getHeaders(),
            		proxy.getSocketHost(), proxy.getSocketPort(),
            		request.getRequestBodyEncoding());
        	}
        } else if (method.equalsIgnoreCase(HttpConstant.Method.TRACE)) {
        	SocketProxy proxy = buildSocketProxy(site, request);
        	if(null == proxy) {
        		result = HttpClientUtils.trace(url,request.getHeaders(),null,-1,
        			request.getRequestBodyEncoding());
        	} else {
        		result = HttpClientUtils.trace(url, request.getHeaders(),
            		proxy.getSocketHost(), proxy.getSocketPort(),
            		request.getRequestBodyEncoding());
        	}
        } else {
        	throw new IllegalArgumentException("HTTP Method{" + method + "} did not be supported.");
        }
        return result;
    }

    @Override
    public void setThreadNum(int thread) {
        httpClientGenerator.setPoolSize(thread);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: statusAccept
     * @Description: 判断响应状态码是否接受(不被接受的响应状态码,则不会提取网页)
     * @param @param acceptStatCode
     * @param @param statusCode
     * @param @return
     * @return boolean
     * @throws
     */
    protected boolean statusAccept(Set<Integer> acceptStatCode, int statusCode) {
        return acceptStatCode.contains(statusCode);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getHttpUriRequest
     * @Description: 创建Http请求对象
     * @param @param request
     * @param @param site
     * @param @return
     * @return HttpUriRequest
     * @throws
     */
    protected HttpUriRequest getHttpUriRequest(Request request, Site site) {
        RequestBuilder requestBuilder = selectRequestMethod(request).setUri(request.getUrl());
        //设置请求头信息
        if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
            for (Map.Entry<String, String> headerEntry : request.getHeaders().entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        //若是Ajax请求,则需要添加一个请求头信息
        if(request.isAjax()) {
        	requestBuilder.addHeader("x-requested-with", "XMLHttpRequest");
        }
        
        RequestConfig.Builder requestConfigBuilder = httpClientGenerator.createDefaultRequestConfigBuilder();
        //若设置了Http代理池
        if (site.getHttpProxyPool() != null && site.getHttpProxyPool().isEnable()) {
            HttpHost host = site.getHttpProxyFromPool();
			requestConfigBuilder.setProxy(host);
			request.putExtra(Request.PROXY, host);
		} 
        //若设置了单个Http代理
        else if(site.getHttpProxy()!= null){
            HttpHost host = site.getHttpProxy();
			requestConfigBuilder.setProxy(host);
			request.putExtra(Request.PROXY, host);	
		}
        return requestBuilder.build();
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: selectRequestMethod
     * @Description: 选择确定Http method
     * @param @param request
     * @param @return
     * @return RequestBuilder
     * @throws
     */
    protected RequestBuilder selectRequestMethod(Request request) {
        String method = request.getMethod();
        if (method == null || method.equalsIgnoreCase(HttpConstant.Method.GET)) {
            //默认采用GET方式发起Http请求
            return RequestBuilder.get();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.POST)) {
            RequestBuilder requestBuilder = RequestBuilder.post();
            //设置请求参数
            HttpEntity entity = assemblyParameter(request);
            if(null != entity) {
            	requestBuilder.setEntity(entity);
            }
            return requestBuilder;
        } else if (method.equalsIgnoreCase(HttpConstant.Method.HEAD)) {
            return RequestBuilder.head();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.PUT)) {
            return RequestBuilder.put();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.DELETE)) {
            return RequestBuilder.delete();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.TRACE)) {
            return RequestBuilder.trace();
        }
        throw new IllegalArgumentException("Illegal HTTP Method " + method);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: handleResponse
     * @Description: 处理Http响应体
     * @param @param request
     * @param @param charset
     * @param @param result
     * @param @return
     * @param @throws IOException
     * @return Page
     * @throws
     */
    protected Page handleResponse(Request request, String charset, Result result) throws IOException {
    	//处理GZip解压缩
    	//handleGZip(result);
    	List<String> list = getContent(charset, result);
    	String charsetEncode = list.get(0);
    	String content = list.get(1);
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        //是否GET请求
        boolean isget = HttpConstant.Method.GET.equalsIgnoreCase(request.getMethod());
        page.setHttpGet(isget);
        //若为POST,需要设置请求参数
        if(!isget) {
        	Map<String,String> params = request.getParams();
        	if(null != params && !params.isEmpty()) {
        		page.setParams(params);
        	}
        }
        page.setPageType(request.getPageType());
        page.setContentEncoding(charsetEncode);
        page.setContentLength(result.getHttpEntity().getContentLength());
        Header contentTypeHeader = result.getHttpEntity().getContentType();
        page.setContentType((null == contentTypeHeader)? "" : contentTypeHeader.getValue());
        page.setStatusCode(result.getStatusCode());
        return page;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: handleGZip
     * @Description: 处理Http响应内容的GZip解压缩
     * @param @param result
     * @param @throws HttpException
     * @return void
     * @throws
     */
    protected void handleGZip(Result result) {
    	HttpEntity entity = result.getHttpEntity();
    	if (entity != null && entity.getContentLength() != 0) {
            final Header ceheader = entity.getContentEncoding();
            if (ceheader != null) {
                final HeaderElement[] codecs = ceheader.getElements();
                boolean uncompressed = false;
                for (final HeaderElement codec : codecs) {
                    final String codecname = codec.getName().toLowerCase(Locale.US);
                    if ("gzip".equals(codecname) || "x-gzip".equals(codecname)) {
                    	result.setHttpEntity(new GzipDecompressingEntity(entity));
                        uncompressed = true;
                        break;
                    } else if ("deflate".equals(codecname)) {
                    	result.setHttpEntity(new DeflateDecompressingEntity(entity));
                        uncompressed = true;
                        break;
                    } else if ("identity".equals(codecname)) {
                    	// do nothing else.
                        return;
                    } else {
                        LogUtils.warn("Unsupported Content-Coding: " + codec.getName());
                    }
                }
                if (uncompressed) {
                	result.removeHeader("Content-Length");
                	result.removeHeader("Content-Encoding");
                	result.removeHeader("Content-MD5");
                }
            }
        }
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getContent
     * @Description: 从响应体中获取网页内容
     * @param @param charset
     * @param @param result
     * @param @return
     * @param @throws IOException
     * @return String
     * @throws
     */
    protected List<String> getContent(String charset, Result result) throws IOException {
        List<String> list = new ArrayList<String>(2);
        HttpEntity entity = result.getHttpEntity();
        
        //如果没有传入字符编码
        StringEntityHandler entityHandler = new StringEntityHandler(charset);
        String content = entityHandler.handleEntity(entity);
        //若返回网页内容为空
        if(StringUtils.isEmpty(content)) {
        	list.add(Charset.defaultCharset().name());
        } else {
        	list.add(entityHandler.getEncoding());
        }
    	list.add(content);
    	return list;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getHtmlCharset
     * @Description: 确定网页的提取字符集编码(确保提取到的网页内容不会乱码,这很关键)
     * @param @param httpResponse
     * @param @param contentBytes
     * @param @return
     * @param @throws IOException
     * @return String
     * @throws
     */
    protected String getHtmlCharset(HttpResponse httpResponse, byte[] contentBytes) throws IOException {
        // 从content-type中提取字符集编码
        String contentType = httpResponse.getEntity().getContentType().getValue();
        String charset = StringUtils.getCharsetFromContentType(contentType);
        if (StringUtils.isNotEmpty(charset)) {
        	LogUtils.debug("Auto get charset from content-type: {" + charset + "}");
            return charset;
        }
        
        Charset defaultCharset = Charset.defaultCharset();
        String content = new String(contentBytes, defaultCharset.name());
        if (StringUtils.isNotEmpty(content)) {
            charset = StringUtils.getCharsetFromMeta(content);
            LogUtils.debug("Auto get charset from meta: {" + charset + "}");
        }
        //若字符编码仍然为空,使用字符集编码探测器来自动探测字符集编码
        if(StringUtils.isEmpty(charset)) {
        	Charset charsetObj = CharsetDetectorFacade.getInstance().detectCharset(httpResponse.getEntity().getContent());
        	if(charsetObj == null) {
        		//若自动探测器返回为空,则采用配置文件spider4j.properties中配置的网页默认提取字符编码
        		charset = this.config.getPageDefaultEncoding();
        	} else {
        		charset = charsetObj.name();
        	}
        }
        return charset;
    }
    
    /**
     * 设置HTTP请求链接参数
     */
	private void setHttpParams(HttpParams httpParams,String charset) {
    	if(null == httpParams) {
    		return;
    	}
    	// 设置连接超时时间  
        HttpConnectionParams.setConnectionTimeout(httpParams, this.config.getConnectionTimeout());  
        // 设置读取超时时间  
        HttpConnectionParams.setSoTimeout(httpParams, this.config.getSocketTimeout());
    	// 模拟浏览器，解决一些服务器程序只允许浏览器访问的问题  
    	httpParams.setParameter(CoreProtocolPNames.USER_AGENT, this.config.getUserAgent());
    	httpParams.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, this.config.isExpectContinueEnabled());  
    	httpParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,charset == null ? this.config.getDefaultEncoding() : charset); 
    	//禁用陈旧链接检查(据API介绍,禁用此项能节省30ms)
    	httpParams.setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, this.config.isStaleConnectionCheckEnabled());
    	//数据将会更早发送，增加了带宽消耗
    	httpParams.setParameter("http.tcp.nodelay", this.config.isSocketTcpNoDelay());
        //启用自动重定向
    	httpParams.setParameter(ClientPNames.HANDLE_REDIRECTS, this.config.isRedirectsEnabled());
        //Cookie自动设置策略(BROWSER_COMPATIBILITY:最大程度的兼容各种浏览器供应商的Cookie规范)
    	httpParams.setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.BROWSER_COMPATIBILITY);
    	httpParams.setParameter("http.protocol.single-cookie-header", true);   
        // 设置最大连接数  
        ConnManagerParams.setMaxTotalConnections(httpParams, this.config.getMaxTotalConnections());  
        // 设置获取连接的最大等待时间  
        ConnManagerParams.setTimeout(httpParams, this.config.getConnectionRequestTimeout()); 
        // 设置每个路由最大连接数  
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(this.config.getMaxConnectionsPerHost());  
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams,connPerRoute); 
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: enableSocketProxy
     * @Description: 启用Socket代理
     * @param @param httpClient
     * @return void
     * @throws
     */
	private static void enableSocketProxy(CloseableHttpClient httpClient) {
		httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("http", 80, new SocksSchemeSocketFactory())); 
	}
    
    /**重写验证方法，取消检测ssl*/
    private static TrustManager truseAllManager = new X509TrustManager(){  
        public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {}  
        public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {}  
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;  
        }
    };
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: enableSSL
     * @Description: 支持访问https的网站 
     * @param @param httpClient
     * @return void
     * @throws
     */
	private static void enableSSL(CloseableHttpClient httpClient){  
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
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: assemblyParameter
     * @Description: 组装POST表单参数
     * @param @param request
     * @param @return
     * @return HttpEntity
     * @throws
     */
    private HttpEntity assemblyParameter(Request request) {
    	//若表单中包含了文件域(即<input type="file />")
    	if(request.isIncludeFile()) {
    		//设置普通表单请求参数
    		MultipartEntity reqEntity = new MultipartEntity();
    		if(GerneralUtils.isNotEmptyMap(request.getParams())) {
    			for (String key : request.getParams().keySet()) {
    				reqEntity.addPart(key, StringBody.create(request.getParams().get(key), "text/plain", Charset.forName(request.getRequestBodyEncoding()))); 
    			}
    		}
    		//设置文件域参数
    		if(GerneralUtils.isNotEmptyCollection(request.getFileParams())) {
    			for(String file : request.getFileParams()) {  
    	            reqEntity.addPart(request.getFileFormName(), new FileBody(new File(file)));  
    	        }  
    		}
    		return reqEntity;
    	}
    	if(GerneralUtils.isNotEmptyMap(request.getParams())) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (String key : request.getParams().keySet()) {
				String val = request.getParams().get(key);
				NameValuePair pair = new BasicNameValuePair(key,val);
				parameters.add(pair);
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, Charset.defaultCharset());
			return formEntity;
		}
    	return null;
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: assemblyParameter
     * @Description: 组装请求参数[适用于GET请求]
     * @param @param parameters
     * @param @return
     * @return String
     * @throws
     */
	private String assemblyParameter(Map<String,String> parameters){
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
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: assemblyHeader
     * @Description: 组装请求头信息
     * @param @param headers
     * @param @return
     * @return Header[]
     * @throws
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
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: assemblyCookie
     * @Description: 组装Cookie
     * @param @param cookies
     * @param @return
     * @return String
     * @throws
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
	 * Author: Lanxiaowei(736031305@qq.com)
	 * @Title: buildSocketProxy
	 * @Description: 创建Socket代理对象
	 * @param @param site
	 * @param @param request
	 * @param @return
	 * @return SocketProxy
	 * @throws
	 */
	private SocketProxy buildSocketProxy(Site site,Request request) {
		SocketProxy proxy = null;
		HttpHost host = null;
		//若设置了Http代理池
        if (site.getHttpProxyPool() != null && site.getHttpProxyPool().isEnable()) {
            host = site.getHttpProxyFromPool();
		} 
        //若设置了单个Http代理
        else if(site.getHttpProxy()!= null){
            host = site.getHttpProxy();	
		}
        if(null != host) {
        	proxy = new SocketProxy(host.getHostName(), host.getPort());
			request.putExtra(Request.PROXY, host);
        }
        return proxy;
	}
}
