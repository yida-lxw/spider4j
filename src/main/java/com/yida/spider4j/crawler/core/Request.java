package com.yida.spider4j.crawler.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yida.spider4j.crawler.utils.HttpConstant;
import com.yida.spider4j.crawler.utils.common.GerneralUtils;

/**
 * @ClassName: Request
 * @Description: 抓取请求包装类(包含了待抓取链接)
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年9月29日 下午4:44:33
 *
 */
public class Request implements Serializable {
	private static final long serialVersionUID = 755677993112571172L;
	
	public static final String CYCLE_TRIED_TIMES = "_cycle_tried_times";
    public static final String STATUS_CODE = "statusCode";
    public static final String PROXY = "proxy";
    
    /**网页类型:[起始页,列表页,详情页],暂时只支持这3种类型*/
    private PageType pageType;
    
    /**当前抓取请求对应的URL*/
    private String url;
    
    /**Http method: get / post*/
    private String method = HttpConstant.Method.GET;

    /**http请求参数*/
    private Map<String, String> params;
    
    /**文件域的表单元素名称*/
	private String fileFormName = "file";
	
	/**文件上传后的目标路径*/
	private List<String> fileParams;
    
    /**用于存储一些额外的信息*/
    private Map<String, Object> extras;
    
    /**请求头信息*/
    private Map<String,String> headers;

    /**抓取请求的优先级,值越大越优先处理*/
    private long priority;
    
    /**Http请求实体编码字符集*/
    private String requestBodyEncoding = "UTF-8";
    
    /**表单中是否包含文件域*/
    private boolean includeFile;
    
    /**是否为Ajax请求,若为Ajax请求,需要添加一个请求头信息x-requested-with:XMLHttpRequest*/
    private boolean ajax;
    
    /**请求的MIME类型*/
    private String contentType;

    public Request() {}

    public Request(String url) {
        this.url = url;
    }

    public Request(String url, String method) {
		this.url = url;
		this.method = method;
	}
    
    public Request(String url,PageType pageType) {
        this.url = url;
        this.pageType = pageType;
    }

	public Request(String url, PageType pageType, String method) {
		this.url = url;
		this.pageType = pageType;
		this.method = method;
	}

	public Request(String url, PageType pageType, String method,
			String requestBodyEncoding) {
		this.url = url;
		this.pageType = pageType;
		this.method = method;
		this.requestBodyEncoding = requestBodyEncoding;
	}

	public Request(String url, PageType pageType, String method,
			String requestBodyEncoding, boolean ajax) {
		this.url = url;
		this.pageType = pageType;
		this.method = method;
		this.requestBodyEncoding = requestBodyEncoding;
		this.ajax = ajax;
	}

	public Request(String url, String method, long priority) {
		this.url = url;
		this.method = method;
		this.priority = priority;
	}

	public Request(String url, String method, Map<String, String> headers) {
		this.url = url;
		this.method = method;
		this.headers = headers;
	}

	public Request(String url, String method, Map<String, String> headers,
			Map<String, String> params) {
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.params = params;
	}

	public Request(String url, String method, Map<String, String> headers,
			Map<String, String> params, boolean ajax) {
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.params = params;
		this.ajax = ajax;
	}

	public Request(String url, String method, Map<String, String> headers,
			Map<String, String> params, boolean ajax, String requestBodyEncoding) {
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.params = params;
		this.ajax = ajax;
		this.requestBodyEncoding = requestBodyEncoding;
	}
	
	public Request(String url, String method, Map<String, String> headers,
			Map<String, String> params, boolean ajax, String requestBodyEncoding,PageType pageType) {
		this.url = url;
		this.method = method;
		this.headers = headers;
		this.params = params;
		this.ajax = ajax;
		this.requestBodyEncoding = requestBodyEncoding;
		this.pageType = pageType;
	}
	
	public PageType getPageType() {
		return pageType;
	}

	public Request setPageType(PageType pageType) {
		this.pageType = pageType;
		return this;
	}

	public long getPriority() {
        return priority;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setPriority
     * @Description: 设置请求处理的优先级,值越大越优先处理
     * @param @param priority
     * @param @return
     * @return Request
     * @throws
     */
    public Request setPriority(long priority) {
        this.priority = priority;
        return this;
    }
    
    public String getHeader(String key) {
        if (headers == null) {
            return null;
        }
        return headers.get(key);
    }

    public String getParam(String key) {
        if (params == null) {
            return null;
        }
        return params.get(key);
    }
    
    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }
    
    public Request putHeader(String key, String value) {
        if (headers == null) {
        	headers = new HashMap<String, String>();
        }
        headers.put(key, value);
        return this;
    }

    public Request putParam(String key, String value) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        params.put(key, value);
        return this;
    }
    
    public Request putExtra(String key, Object value) {
        if (extras == null) {
        	extras = new HashMap<String, Object>();
        }
        extras.put(key, value);
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Request setUrl(String url) {
        this.url = url;
		return this;
    }

    public String getMethod() {
        return method;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public Map<String, String> getHeaders() {
		return headers;
	}

	public Request setHeaders(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public Request setParams(Map<String, String> params) {
		this.params = params;
		return this;
	}

	public String getFileFormName() {
		return fileFormName;
	}

	public Request setFileFormName(String fileFormName) {
		this.fileFormName = fileFormName;
		return this;
	}

	public List<String> getFileParams() {
		return fileParams;
	}

	public Request setFileParams(List<String> fileParams) {
		this.fileParams = fileParams;
		return this;
	}

	public Map<String, Object> getExtras() {
		return extras;
	}

	public Request setExtras(Map<String, Object> extras) {
		this.extras = extras;
		return this;
	}
	
	public String getRequestBodyEncoding() {
		return requestBodyEncoding;
	}

	public Request setRequestBodyEncoding(String requestBodyEncoding) {
		this.requestBodyEncoding = requestBodyEncoding;
		return this;
	}

	public boolean isIncludeFile() {
		return includeFile;
	}

	public Request setIncludeFile(boolean includeFile) {
		this.includeFile = includeFile;
		return this;
	}

	public boolean isAjax() {
		return ajax;
	}

	public Request setAjax(boolean ajax) {
		this.ajax = ajax;
		return this;
	}
	
	public String getContentType() {
		return contentType;
	}

	public Request setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	@Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", extras=" + params +
                ", priority=" + priority +
                '}';
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ajax ? 1231 : 1237);
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result
				+ ((pageType == null) ? 0 : pageType.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Request other = (Request) obj;
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		if (ajax != other.ajax) {
			return false;
		}
		if (method == null) {
			if (other.method != null) {
				return false;
			}
		} else if (!method.equals(other.method)) {
			return false;
		}
		if (pageType != other.pageType)
			return false;
		if (params == null) {
			if (other.params != null) {
				return false;
			}
		} else {
			return GerneralUtils.mapEquals(this.params, other.getParams());
		}
		return true;
	}
}
