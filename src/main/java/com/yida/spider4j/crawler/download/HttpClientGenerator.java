package com.yida.spider4j.crawler.download;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;

import com.yida.spider4j.crawler.config.DefaultConfigurable;
import com.yida.spider4j.crawler.core.Site;

/**
 * @ClassName: HttpClientGenerator
 * @Description: HttpClient实例生成器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月9日 下午3:11:21
 *
 */
public class HttpClientGenerator extends DefaultConfigurable {
	/**Http链接池管理器*/
    private PoolingHttpClientConnectionManager connectionManager;

    public HttpClientGenerator() {
    	boolean enableHttps = this.config.isIncludeHttpsPages();
    	Registry<ConnectionSocketFactory> reg = null;
    	if(enableHttps) {
    		reg = RegistryBuilder.<ConnectionSocketFactory>create()
               .register("http", PlainConnectionSocketFactory.INSTANCE)
               .register("https", SSLConnectionSocketFactory.getSocketFactory())
                   .build();
    	} else {
    		reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .build();   
    	}
    	connectionManager = new PoolingHttpClientConnectionManager(reg);
    }

    public HttpClientGenerator setPoolSize(int poolSize) {
        connectionManager.setMaxTotal(poolSize);
        return this;
    }

    public CloseableHttpClient getClient(Site site) {
        return generateClient(site);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: generateClient
     * @Description: 生成HttpClient对象
     * @param @param site
     * @param @return
     * @return CloseableHttpClient
     * @throws
     */
    private CloseableHttpClient generateClient(Site site) {   
    	connectionManager.setDefaultMaxPerRoute(this.config.getMaxConnectionsPerHost());
        connectionManager.setMaxTotal(this.config.getMaxTotalConnections());
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager);

        if (site != null && site.getUserAgent() != null) {
            httpClientBuilder.setUserAgent(site.getUserAgent());
        } else {
            httpClientBuilder.setUserAgent(this.config.getUserAgent());
        }
        if (site !=null && site.isUseGzip()) {
            addGZipHeader(httpClientBuilder);
        } else {
        	boolean useGZip = this.config.isUseGZip();
        	//若用户配置了启用GZip压缩
        	if(useGZip) {
        		addGZipHeader(httpClientBuilder);
        	}
        }
        
        // Socket相关参数配置
        SocketConfig.Builder builder = SocketConfig.custom();
        builder.setSoKeepAlive(this.config.getIdleThreadKeepAliveTime() > 0);
        builder.setSoLinger((int)this.config.getSocketCloseDelay());
        builder.setSoReuseAddress(this.config.isSocketReuseAddress());
        builder.setTcpNoDelay(this.config.isSocketTcpNoDelay());
        builder.setSoTimeout(this.config.getSocketTimeout());
        httpClientBuilder.setDefaultSocketConfig(builder.build());
        
        if (site != null) {
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(site.getRetryTimes(), true));
        }
        
        generateCookie(httpClientBuilder, site);
        return httpClientBuilder.build();
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: createDefaultRequestConfigBuilder
     * @Description: 设置默认的Http请求相关参数配置
     * @param @param httpClientBuilder
     * @return void
     * @throws
     */
    public RequestConfig.Builder createDefaultRequestConfigBuilder() {
    	// Http相关参数配置
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        requestBuilder.setExpectContinueEnabled(this.config.isExpectContinueEnabled());
        requestBuilder.setStaleConnectionCheckEnabled(this.config.isStaleConnectionCheckEnabled());
        requestBuilder.setRedirectsEnabled(this.config.isRedirectsEnabled());
        requestBuilder.setRelativeRedirectsAllowed(this.config.isRelativeRedirectsAllowed());
        requestBuilder.setCircularRedirectsAllowed(this.config.isCircularRedirectsAllowed());
        requestBuilder.setMaxRedirects(this.config.getMaxRedirects());
        requestBuilder.setAuthenticationEnabled(this.config.isAuthenticationEnabled());
        requestBuilder.setConnectionRequestTimeout(this.config.getConnectionRequestTimeout());
        requestBuilder.setConnectTimeout(this.config.getConnectTimeout());
        requestBuilder.setCookieSpec(CookieSpecs.BEST_MATCH);
        return requestBuilder;
        //httpClientBuilder.setDefaultRequestConfig(requestBuilder.build());
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: addGZipHeader
     * @Description: 添加gzip编码请求头
     * @param @param httpClientBuilder
     * @return void
     * @throws
     */
	private void addGZipHeader(HttpClientBuilder httpClientBuilder) {
		httpClientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {
		    public void process(
		            final HttpRequest request,
		            final HttpContext context) throws HttpException, IOException {
		        if (!request.containsHeader("Accept-Encoding")) {
		            request.addHeader("Accept-Encoding", "gzip, deflate");
		        }
		    }
		});
	}

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: generateCookie
     * @Description: 设置Cookie
     * @param @param httpClientBuilder
     * @param @param site
     * @return void
     * @throws
     */
    private void generateCookie(HttpClientBuilder httpClientBuilder, Site site) {
    	if(null == site) {
    		return;
    	}
        CookieStore cookieStore = new BasicCookieStore();
        if(null != site.getCookies() && !site.getCookies().isEmpty()) {
        	for (Map.Entry<String, String> cookieEntry : site.getCookies().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                cookie.setDomain(site.getDomain());
                cookieStore.addCookie(cookie);
            }
        }
        
        if(null != site.getAllCookies() && !site.getAllCookies().isEmpty()) {
        	for (Map.Entry<String, Map<String, String>> domainEntry : site.getAllCookies().entrySet()) {
                for (Map.Entry<String, String> cookieEntry : domainEntry.getValue().entrySet()) {
                    BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                    cookie.setDomain(domainEntry.getKey());
                    cookieStore.addCookie(cookie);
                }
            }
        }
        httpClientBuilder.setDefaultCookieStore(cookieStore);
    }
}
