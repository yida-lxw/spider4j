package com.yida.spider4j.crawler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.yida.spider4j.crawler.selector.ExpressionType;
import com.yida.spider4j.crawler.selector.Html;
import com.yida.spider4j.crawler.selector.Json;
import com.yida.spider4j.crawler.selector.Selectable;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.url.URLUtils;


/**
 * @ClassName: Page
 * @Description: 爬虫抓取到的页面
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月30日 上午9:26:15
 *
 */
public class Page {
	private Request request;

	/**用于存储从当前网页提取出来的数据*/
    private PageResultItem resultItem;

    private Html html;

    private Json json;

    /**当前网页的内容*/
    private String rawText;

    /**当前网页对应的URL链接*/
    private Selectable url;

    /**返回的HTTP响应状态码*/
    private int statusCode;
    
    /**响应内容的字节长度*/
    private long contentLength;
    
    /**响应内容的MIME类型,如application/json*/
    private String contentType;
    
    /**响应内容的字符集编码*/
    private String contentEncoding;

    /**是否需要周期性不间断的抓取*/
    private boolean needCycleRetry;

    /**响应头信息*/
    private Map<String ,String> responseHeader;
    
    /**POST请求参数*/
    private Map<String,String> params;
    
    private List<Request> targetRequests;
    
    /**页面类型*/
    private PageType pageType;
    
    /**是否为GET请求获取的*/
    private boolean httpGet;
    
    public Page() {
    	this.resultItem = new PageResultItem();
    	this.targetRequests = new ArrayList<Request>();
    	this.responseHeader = new HashMap<String, String>();
    }

    public Page setSkip(boolean skip) {
        resultItem.setSkip(skip);
        return this;

    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: putField
     * @Description: 存放提取到的数据
     * @param @param key
     * @param @param field
     * @return void
     * @throws
     */
    public Page putField(String key, Object field) {
        resultItem.put(key, field);
        return this;
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: putMap
     * @Description: 存放提取到的数据
     * @param @param map
     * @param @return
     * @return Page
     * @throws
     */
    public Page putMap(LinkedHashMap<String, Object> map) {
    	if(null != map && !map.isEmpty()) {
    		 resultItem.putMap(map);
    	}
        return this;
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getValueByField
     * @Description: 根据field获取提取的数据值
     * @param @param field
     * @param @return
     * @return Object
     * @throws
     */
    public Object getValueByField(String field) {
    	if(null == resultItem || resultItem.getDataMap() == null || 
    			resultItem.getDataMap().isEmpty()) {
    		return null;
    	}
    	if(!resultItem.getDataMap().containsKey(field)) {
    		return null;
    	}
    	return resultItem.getDataMap().get(field);
    }
    
    public Page putHeader(String key,String val) {
    	if(null == this.responseHeader) {
    		this.responseHeader = new HashMap<String,String>();
    	}
    	this.responseHeader.put(key, val);
    	return this;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getHtml
     * @Description: 返回网页的HTML内容
     * @param @return
     * @return Html
     * @throws
     */
    public Html getHtml(ExpressionType type) {
        if (html == null) {
            html = new Html(URLUtils.fixAllRelativeHrefs(rawText, request.getUrl()),type);
        }
    	//html = new Html(URLUtils.fixAllRelativeHrefs(rawText, request.getUrl()),type);
        return html;
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getHtml
     * @Description: 返回网页的HTML内容
     * @param @return
     * @return Html
     * @throws
     */
    public Html getHtml() {
        if (html == null) {
            html = new Html(URLUtils.fixAllRelativeHrefs(rawText, request.getUrl()));
        }
    	//html = new Html(URLUtils.fixAllRelativeHrefs(rawText, request.getUrl()));
        return html;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getJson
     * @Description: 获取HTTP返回的JSON数据
     * @param @return
     * @return Json
     * @throws
     */
    public Json getJson() {
        if (json == null) {
            json = new Json(rawText);
        }
        return json;
    }

    public Page setHtml(Html html) {
        this.html = html;
        return this;
    }
    
    public Page setJson(Json json) {
    	this.json = json;
    	return this;
    }

    public List<Request> getTargetRequests() {
        return targetRequests;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: addTargetRequests
     * @Description: 添加待抓取的URL链接
     * @param @param requests
     * @return void
     * @throws
     */
    public void addTargetRequests(List<String> requests) {
        synchronized (targetRequests) {
            for (String s : requests) {
                if (StringUtils.isBlank(s) || s.equals("#") || s.toLowerCase().startsWith("javascript:")) {
                    continue;
                }
                s = URLUtils.canonicalizeUrl(s, url.toString());
                targetRequests.add(new Request(s));
            }
        }
    }

    public void addTargetRequests(List<String> requests, String method) {
        synchronized (targetRequests) {
            for (String s : requests) {
                if (StringUtils.isBlank(s) || s.equals("#") || s.toLowerCase().startsWith("javascript:")) {
                    continue;
                }
                s = URLUtils.canonicalizeUrl(s, url.toString());
                targetRequests.add(new Request(s).setMethod(method));
            }
        }
    }
    
    public void addTargetRequests(List<String> requests, long priority) {
        synchronized (targetRequests) {
            for (String s : requests) {
                if (StringUtils.isBlank(s) || s.equals("#") || s.toLowerCase().startsWith("javascript:")) {
                    continue;
                }
                s = URLUtils.canonicalizeUrl(s, url.toString());
                targetRequests.add(new Request(s).setPriority(priority));
            }
        }
    }
    
    public void addTargetRequests(List<String> requests, String method, long priority) {
        synchronized (targetRequests) {
            for (String s : requests) {
                if (StringUtils.isBlank(s) || s.equals("#") || s.toLowerCase().startsWith("javascript:")) {
                    continue;
                }
                s = URLUtils.canonicalizeUrl(s, url.toString());
                targetRequests.add(new Request(s).setMethod(method).setPriority(priority));
            }
        }
    }
    
    public void addTargetRequests(List<String> requests, String method, long priority,Map<String, String> headers) {
        synchronized (targetRequests) {
            for (String s : requests) {
                if (StringUtils.isBlank(s) || s.equals("#") || s.toLowerCase().startsWith("javascript:")) {
                    continue;
                }
                s = URLUtils.canonicalizeUrl(s, url.toString());
                targetRequests.add(new Request(s)
                	.setMethod(method).setPriority(priority)
                	.setHeaders(headers)
                );
            }
        }
    }
    
    public void addTargetRequests(List<String> requests, String method, long priority,
    	Map<String, String> headers,Map<String, String> params) {
        synchronized (targetRequests) {
            for (String s : requests) {
                if (StringUtils.isBlank(s) || s.equals("#") || s.toLowerCase().startsWith("javascript:")) {
                    continue;
                }
                s = URLUtils.canonicalizeUrl(s, url.toString());
                targetRequests.add(new Request(s)
                	.setMethod(method).setPriority(priority)
                	.setHeaders(headers).setParams(params)
                );
            }
        }
    }
    
    public void addTargetRequests(List<String> requests, String method, long priority,
    	Map<String, String> headers,Map<String, String> params,boolean ajax) {
        synchronized (targetRequests) {
            for (String s : requests) {
                if (StringUtils.isBlank(s) || s.equals("#") || s.toLowerCase().startsWith("javascript:")) {
                    continue;
                }
                s = URLUtils.canonicalizeUrl(s, url.toString());
                targetRequests.add(new Request(s)
                	.setMethod(method).setPriority(priority)
                	.setHeaders(headers).setParams(params)
                	.setAjax(ajax)
                );
            }
        }
    }
    
    public void addTargetRequests(List<String> requests, String method, long priority,
        	Map<String, String> headers,Map<String, String> params,boolean ajax,String requestBodyEncoding) {
            synchronized (targetRequests) {
                for (String s : requests) {
                    if (StringUtils.isBlank(s) || s.equals("#") || s.toLowerCase().startsWith("javascript:")) {
                        continue;
                    }
                    s = URLUtils.canonicalizeUrl(s, url.toString());
                    targetRequests.add(new Request(s)
                    	.setMethod(method).setPriority(priority)
                    	.setHeaders(headers).setParams(params)
                    	.setAjax(ajax).setRequestBodyEncoding(requestBodyEncoding)
                    );
                }
            }
    }
    
    public void addTargetRequests(List<String> requests, String method, long priority,
        	Map<String, String> headers,Map<String, String> params,boolean ajax,String requestBodyEncoding,
        	PageType pageType) {
            synchronized (targetRequests) {
                for (String s : requests) {
                    if (StringUtils.isBlank(s) || s.equals("#") || s.toLowerCase().startsWith("javascript:")) {
                        continue;
                    }
                    s = URLUtils.canonicalizeUrl(s, url.toString());
                    targetRequests.add(new Request(s)
                    	.setMethod(method).setPriority(priority)
                    	.setHeaders(headers).setParams(params)
                    	.setAjax(ajax).setRequestBodyEncoding(requestBodyEncoding)
                    	.setPageType(pageType)
                    );
                }
            }
    }
    
    public void addTargetRequests(String url, String method, long priority,
        	Map<String, String> headers,List<Map<String,String>>  paramList,boolean ajax,String requestBodyEncoding,
        	PageType pageType) {
            synchronized (targetRequests) {
            	if (StringUtils.isBlank(url) || url.equals("#") || url.toLowerCase().startsWith("javascript:")) {
    	            return;
    	        }
            	url = URLUtils.canonicalizeUrl(url, url.toString());
                for (Map<String,String> param : paramList) {	
                	if(null == param || param.isEmpty()) {
                		continue;
                	}
                    targetRequests.add(new Request(url)
                    	.setMethod(method).setPriority(priority)
                    	.setHeaders(headers).setParams(param)
                    	.setAjax(ajax).setRequestBodyEncoding(requestBodyEncoding)
                    	.setPageType(pageType)
                    );
                }
            }
    }

    /**
     * add url to fetch
     *
     * @param requestString
     */
    public void addTargetRequest(String requestString) {
        if (StringUtils.isBlank(requestString) || requestString.equals("#") ||
        	requestString.toLowerCase().startsWith("javascript:")) {
            return;
        }
        synchronized (targetRequests) {
            requestString = URLUtils.canonicalizeUrl(requestString, url.toString());
            targetRequests.add(new Request(requestString));
        }
    }

    public void addTargetRequest(Request request) {
        synchronized (targetRequests) {
            targetRequests.add(request);
        }
    }
    
    public void addTargetRequestList(List<Request> requests) {
        synchronized (targetRequests) {
            targetRequests.addAll(requests);
        }
    }

    /**
     * get url of current page
     *
     * @return url of current page
     */
    public Selectable getUrl() {
        return url;
    }

    public void setUrl(Selectable url) {
        this.url = url;
    }

    /**
     * get request of current page
     *
     * @return request
     */
    public Request getRequest() {
        return request;
    }

    public boolean isNeedCycleRetry() {
        return needCycleRetry;
    }

    public void setNeedCycleRetry(boolean needCycleRetry) {
        this.needCycleRetry = needCycleRetry;
    }

    public void setRequest(Request request) {
        this.request = request;
        this.resultItem.setRequest(request);
    }

    public PageResultItem getPageResultItem() {
        return resultItem;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRawText() {
        return rawText;
    }

    public Page setRawText(String rawText) {
        this.rawText = rawText;
        return this;
    }

    public PageResultItem getResultItem() {
		return resultItem;
	}

	public void setResultItem(PageResultItem resultItem) {
		this.resultItem = resultItem;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	/*public void setJson(Json json) {
		this.json = json;
	}*/
	
	public void setTargetRequests(List<Request> targetRequests) {
		this.targetRequests = targetRequests;
	}

	public Map<String, String> getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(Map<String, String> responseHeader) {
		this.responseHeader = responseHeader;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public Page setParams(Map<String, String> params) {
		this.params = params;
		return this;
	}
	
	/**
	 * @Author: Lanxiaowei(736031305@qq.com)
	 * @Title: getParamValue
	 * @Description: 根据key获取POST参数值
	 * @param @param key
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getParamValue(String key) {
		if(null == params || params.isEmpty()) {
			return null;
		}
		return this.params.get(key);
	}
	
	public Page putParamValue(String key,String value) {
		if(null != this.params && !this.params.isEmpty()) {
			this.params.put(key, value);
		}
		return this;
	}

	public PageType getPageType() {
		return pageType;
	}

	public Page setPageType(PageType pageType) {
		this.pageType = pageType;
		return this;
	}

	public boolean isHttpGet() {
		return httpGet;
	}

	public void setHttpGet(boolean httpGet) {
		this.httpGet = httpGet;
	}

	@Override
    public String toString() {
        return "Page{" +
                "request=" + request +
                ", resultItem=" + resultItem +
                ", rawText='" + rawText + '\'' +
                ", url=" + url +
                ", statusCode=" + statusCode +
                ", targetRequests=" + targetRequests +
                '}';
    }
}
