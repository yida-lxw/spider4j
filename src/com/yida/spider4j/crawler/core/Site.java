package com.yida.spider4j.crawler.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpHost;

import com.yida.spider4j.crawler.config.DefaultConfigurable;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.proxy.ProxyPool;
import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.map.DoubleKeyMap;
import com.yida.spider4j.crawler.utils.url.URLUtils;


/**
 * @ClassName: WebSite
 * @Description: 要抓取的网站
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月29日 上午9:44:22
 *
 */
public class Site extends DefaultConfigurable {
	/**网站域名,如google.com*/
	private String domain;
	
    private String userAgent;
    
    /**起始页URL*/
    private String startUrl;
    
    /**PageProcessorParam参数对象*/
    private PageProcessorParam pageProcessorParam;
    
    /**起始URL集合*/
    private List<String> startUrls = new ArrayList<String>();
    
    private List<Request> startRequests = new ArrayList<Request>();

    private Map<String, String> defaultCookies = new LinkedHashMap<String, String>();

    private DoubleKeyMap<String, String, String> cookies = new DoubleKeyMap<String, String, String>();

    private String charset;

    /**每抓取一个网页的睡眠休息时间,单位:毫秒*/
    private int sleepTime = 1;
    
    /**HttpClient内部Http请求失败重试次数*/
    private int retryTimes = 0;

    /**周期性抓取的次数*/
    private int cycleRetryTimes;

    /**每抓取一个周期,睡眠多少毫秒*/
    private int retrySleepTime = 0;

    private int timeOut = 5000;

    private static final Set<Integer> DEFAULT_STATUS_CODE_SET = new HashSet<Integer>();

    private Set<Integer> acceptStatCode = DEFAULT_STATUS_CODE_SET;

    private Map<String, String> headers;
    
    private Map<String, String> params;

    private HttpHost httpProxy;

    private ProxyPool httpProxyPool;

    /**是否启用GZip压缩*/
    private boolean useGzip;
    
    /**抓取之前是否需要先登录验证*/
    private boolean mustLoginAuth;
    
    /**起始页是否需要分页*/
    private boolean shouldStartPagePaging;
    
    static {
        DEFAULT_STATUS_CODE_SET.add(200);
    }
    public static Site me() {
        return new Site();
    }

    public Site addCookie(String name, String value) {
        defaultCookies.put(name, value);
        return this;
    }


    public Site addCookie(String domain, String name, String value) {
        cookies.put(domain, name, value);
        return this;
    }

    public String getStartUrl() {
		return startUrl;
	}

	public Site setStartUrl(String startUrl) {
		this.startUrl = startUrl;
		return this;
	}

	public Site setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Map<String, String> getCookies() {
        return defaultCookies;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getDomain() {
        return domain;
    }

    public Site setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setCharset
     * @Description: 设置网页默认编码,若没有设置,则会自动探测
     * @param @param charset
     * @param @return
     * @return Site
     * @throws
     */
    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public Site setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public PageProcessorParam getPageProcessorParam() {
		return pageProcessorParam;
	}

	public Site setPageProcessorParam(PageProcessorParam pageProcessorParam) {
		this.pageProcessorParam = pageProcessorParam;
		return this;
	}

	/**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setAcceptStatCode
     * @Description: 设置可以接受的响应状态码,响应状态码不在指定范围内的网页将不会被处理
     * @param @param acceptStatCode
     * @param @return
     * @return Site
     * @throws
     */
    public Site setAcceptStatCode(Set<Integer> acceptStatCode) {
        this.acceptStatCode = acceptStatCode;
        return this;
    }

    public Set<Integer> getAcceptStatCode() {
        return acceptStatCode;
    }

    /**
     * Set the interval between the processing of two pages.<br>
     * Time unit is micro seconds.<br>
     *
     * @param sleepTime
     * @return this
     */
    public Site setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    /**
     * Get retry times immediately when download fail, 0 by default.<br>
     *
     * @return retry times when download fail
     */
    public int getRetryTimes() {
        return retryTimes;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: addHeader
     * @Description: 添加Http请求头信息
     * @param @param key
     * @param @param value
     * @param @return
     * @return Site
     * @throws
     */
    public Site addHeader(String key, String value) {
    	if(headers == null) {
    		headers = new HashMap<String, String>();
    	}
        headers.put(key, value);
        return this;
    }
    
    public Site addParam(String key, String value) {
    	if(params == null) {
    		params = new HashMap<String, String>();
    	}
    	params.put(key, value);
        return this;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setRetryTimes
     * @Description: 设置网页下载失败后重试次数
     * @param @param retryTimes
     * @param @return
     * @return Site
     * @throws
     */
    public Site setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public int getCycleRetryTimes() {
    	if(this.cycleRetryTimes <= 0) {
    		this.cycleRetryTimes = this.config.getMaxPageFailurRetryTimes();
    	}
        return cycleRetryTimes;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setCycleRetryTimes
     * @Description: 设置循环重试次数
     * @param @param cycleRetryTimes
     * @param @return
     * @return Site
     * @throws
     */
    public Site setCycleRetryTimes(int cycleRetryTimes) {
        this.cycleRetryTimes = cycleRetryTimes;
        return this;
    }

    public HttpHost getHttpProxy() {
        return httpProxy;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setHttpProxy
     * @Description: 设置Http代理
     * @param @param httpProxy
     * @param @return
     * @return Site
     * @throws
     */
    public Site setHttpProxy(HttpHost httpProxy) {
        this.httpProxy = httpProxy;
        return this;
    }

    public boolean isUseGzip() {
        return useGzip;
    }

    public int getRetrySleepTime() {
        return retrySleepTime;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setRetrySleepTime
     * @Description: 重试之前休眠时间,单位毫秒,默认1000
     * @param @param retrySleepTime
     * @param @return
     * @return Site
     * @throws
     */
    public Site setRetrySleepTime(int retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
        return this;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setUseGzip
     * @Description: 开启Http GZip压缩
     * @param @param useGzip
     * @param @return
     * @return Site
     * @throws
     */
    public Site setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;
        return this;
    }

	public List<String> getStartUrls() {
		return startUrls;
	}

	public Site setStartUrls(List<String> startUrls) {
		this.startUrls = startUrls;
		return this;
	}

	public Site setHeaders(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public Site setHttpProxyPool(ProxyPool httpProxyPool) {
		this.httpProxyPool = httpProxyPool;
		return this;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Site site = (Site) o;

        if (cycleRetryTimes != site.cycleRetryTimes) return false;
        if (retryTimes != site.retryTimes) return false;
        if (sleepTime != site.sleepTime) return false;
        if (timeOut != site.timeOut) return false;
        if (acceptStatCode != null ? !acceptStatCode.equals(site.acceptStatCode) : site.acceptStatCode != null)
            return false;
        if (charset != null ? !charset.equals(site.charset) : site.charset != null) return false;
        if (defaultCookies != null ? !defaultCookies.equals(site.defaultCookies) : site.defaultCookies != null)
            return false;
        if (domain != null ? !domain.equals(site.domain) : site.domain != null) return false;
        if (headers != null ? !headers.equals(site.headers) : site.headers != null) return false;
        
        if (userAgent != null ? !userAgent.equals(site.userAgent) : site.userAgent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = domain != null ? domain.hashCode() : 0;
        result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
        result = 31 * result + (defaultCookies != null ? defaultCookies.hashCode() : 0);
        result = 31 * result + (charset != null ? charset.hashCode() : 0);
        result = 31 * result + sleepTime;
        result = 31 * result + retryTimes;
        result = 31 * result + cycleRetryTimes;
        result = 31 * result + timeOut;
        result = 31 * result + (acceptStatCode != null ? acceptStatCode.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Site{" +
                "domain='" + domain + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", cookies=" + defaultCookies +
                ", charset='" + charset + '\'' +
                ", sleepTime=" + sleepTime +
                ", retryTimes=" + retryTimes +
                ", cycleRetryTimes=" + cycleRetryTimes +
                ", timeOut=" + timeOut +
                ", acceptStatCode=" + acceptStatCode +
                ", headers=" + headers +
                '}';
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setHttpProxyPool
     * @Description: 设置Http代理池,String[0]:ip, String[1]:port
     * @param @param httpProxyList
     * @param @return
     * @return Site
     * @throws
     */
    public Site setHttpProxyPool(List<String[]> httpProxyList) {
        this.httpProxyPool=new ProxyPool(httpProxyList);
        return this;
    }

    public Site enableHttpProxyPool() {
        this.httpProxyPool=new ProxyPool();
        return this;
    }

    public ProxyPool getHttpProxyPool() {
        return httpProxyPool;
    }

    public HttpHost getHttpProxyFromPool() {
        return httpProxyPool.getProxy();
    }

    public void returnHttpProxyToPool(HttpHost proxy,int statusCode) {
        httpProxyPool.returnProxy(proxy,statusCode);
    }

    public Site setProxyReuseInterval(int reuseInterval) {
        this.httpProxyPool.setReuseInterval(reuseInterval);
        return this;
    }

	public Map<String, String> getDefaultCookies() {
		return defaultCookies;
	}

	public Site setDefaultCookies(Map<String, String> defaultCookies) {
		this.defaultCookies = defaultCookies;
        return this;
	}

	public Site setCookies(DoubleKeyMap<String, String, String> cookies) {
		this.cookies = cookies;
        return this;
	}
	
	/*public String getStartUrlPrefix() {
		return startUrlPrefix;
	}

	public Site setStartUrlPrefix(String startUrlPrefix) {
		this.startUrlPrefix = startUrlPrefix;
		return this;
	}

	public String getListUrlPrefix() {
		return listUrlPrefix;
	}

	public Site setListUrlPrefix(String listUrlPrefix) {
		this.listUrlPrefix = listUrlPrefix;
		return this;
	}

	public String getDetailUrlPrefix() {
		return detailUrlPrefix;
	}

	public Site setDetailUrlPrefix(String detailUrlPrefix) {
		this.detailUrlPrefix = detailUrlPrefix;
		return this;
	}

	public String getStartUrlRegex() {
		return startUrlRegex;
	}

	public Site setStartUrlRegex(String startUrlRegex) {
		this.startUrlRegex = startUrlRegex;
		return this;
	}

	public String getListUrlRegex() {
		return listUrlRegex;
	}

	public Site setListUrlRegex(String listUrlRegex) {
		this.listUrlRegex = listUrlRegex;
		return this;
	}

	public String getDetailUrlRegex() {
		return detailUrlRegex;
	}

	public Site setDetailUrlRegex(String detailUrlRegex) {
		this.detailUrlRegex = detailUrlRegex;
		return this;
	}*/

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public boolean isMustLoginAuth() {
		return mustLoginAuth;
	}

	public Site setMustLoginAuth(boolean mustLoginAuth) {
		this.mustLoginAuth = mustLoginAuth;
		return this;
	}

	public List<Request> getStartRequests() {
		return startRequests;
	}

	public Site setStartRequests(List<Request> startRequests) {
		this.startRequests = startRequests;
		return this;
	}

	public boolean isShouldStartPagePaging() {
		return shouldStartPagePaging;
	}

	public Site setShouldStartPagePaging(boolean shouldStartPagePaging) {
		this.shouldStartPagePaging = shouldStartPagePaging;
		return this;
	}

	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: addStartUrl
	 * @Description: 添加爬虫的起始URL
	 * @param @param startUrl
	 * @param @return
	 * @return Site
	 * @throws
	 */
	public Site addStartUrl(String startUrl) {
		this.startUrls.add(startUrl);
		return this;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: addStartUrls
	 * @Description: 添加爬虫的多个起始URL
	 * @param @param startUrls
	 * @param @return
	 * @return Site
	 * @throws
	 */
	public Site addStartUrls(List<String> startUrls) {
		if(null != startUrls && startUrls.size() > 0) {
			this.startUrls.addAll(startUrls);
		}
		return this;
	}
	
	public Site addStartRequest(Request startRequest) {
		if(null != startRequest && StringUtils.isNotEmpty(startRequest.getUrl())) {
			this.startRequests.add(startRequest);
	        if (domain == null) {
	            domain = URLUtils.getDomain(startRequest.getUrl());
	        }
		}
        return this;
    }
	
	public Site addStartRequests(Collection<Request> startRequests) {
		if(null != startRequests && startRequests.size() > 0) {
			this.startRequests.addAll(startRequests);
		}
		return this;
	}
	
	public Map<String,Map<String, String>> getAllCookies() {
        return cookies.getMap();
    }
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: toTask
	 * @Description: Site对象转换成Task对象
	 * @param @return
	 * @return Task
	 * @throws
	 */
	public Task toTask() {
        return new Task() {
            @Override
            public String taskId() {
                return Site.this.getDomain();
            }

            @Override
            public Site getSite() {
                return Site.this;
            }
        };
    }
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: requestToUrls
	 * @Description: Request对象集合转换成URL集合
	 * @param @param requests
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public static List<String> requestToUrls(Collection<Request> requests) {
        List<String> urlList = new ArrayList<String>(requests.size());
        for (Request request : requests) {
            urlList.add(request.getUrl());
        }
        return urlList;
    }
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: urlToRequests
	 * @Description: URL集合集合转换成Request对象
	 * @param @param urls
	 * @param @return
	 * @return List<Request>
	 * @throws
	 */
	public static List<Request> urlToRequests(Collection<String> urls) {
        List<Request> requestList = new ArrayList<Request>(urls.size());
        for (String url : urls) {
            requestList.add(new Request(url));
        }
        return requestList;
    }
}
