/*
 * Copyright (c) 2015, Xinlong.inc and/or its affiliates. All rights reserved.
 */
package com.yida.spider4j.crawler.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.yida.spider4j.crawler.auth.param.AuthInfo;

/**
 * 爬虫配置对象
 * 
 * @since 1.0
 * @author Lanxiaowei@citic-finance.com
 * @date 2015-9-28上午9:41:38
 * 
 */
public class CrawlConfig {
	/**爬虫根目录*/
	private String crawlBaseDir;
	/**爬虫数据存储目录 */
	private String crawlStorageFolder;
	/**爬虫断点恢复存储目录*/
	private String crawlResumeFolder;

	/** 是否开启断点续爬功能，默认不开启，若开启，可能会导致爬虫抓取速度变慢 */
	private boolean resumableCrawling;

	/** URL抓取深度,-1表示不限制 */
	private int maxDepthOfCrawling;
	
	/**爬虫抓取网页失败后最大重试次数,默认重试3次*/
	private int maxPageFailurRetryTimes;

	/** 设置最多抓取页面个数,-1表示不限制 */
	private int maxPagesToFetch;

	/** user-agent */
	private String userAgent;

	/** 默认请求头信息 */
	private Collection<BasicHeader> defaultHeaders;

	/** 对于同一主机两次抓取时间间隔:单位毫秒 */
	private int politenessDelay;

	/** 是否需要抓取https页面 */
	private boolean includeHttpsPages;

	/** 是否提取二进制数据 */
	private boolean includeBinaryContentInCrawling;

	/** 是否处理二进制数据 */
	private boolean processBinaryContentInCrawling;

	/** 每个主机的最大链接数 */
	private int maxConnectionsPerHost;

	/** 总的最大Http链接数 */
	private int maxTotalConnections;

	/**Scoket网络读写超时时间，单位：毫秒*/
	private int socketTimeout;

	/**Http链接超时时间，单位：毫秒*/
	private int connectionTimeout;
	
	/**立即发送数据,没有TCP延迟*/
	private boolean socketTcpNoDelay;
	
	/**当调用socket.close()时,是否立即关闭底层的TCP连接,-1表示立即关闭,默认为3600,单位毫秒,即延迟3600毫秒后关闭底层的TCP连接*/
	private long socketCloseDelay;
	
	/**是否允许重用Socket所绑定的本地地址*/
	private boolean socketReuseAddress;
	
	/**
	 * Expect:100-Continue握手的目的，是为了允许客户端在发送请求内容之前，
	 * 判断源服务器是否愿意接受 请求（基于请求头部）。 Expect:100-Continue握手需谨慎使用，
	 * 因为遇到不支持HTTP/1.1协议的服务器或者代理时会引起问题。
	 * 而HttpClient 4.0中，是否激活Expect:100-Continue，
	 * 是由HTTP请求执行参数http.protocol.expect-continue来控制的，
	 * 通过设置参数值为true或者false，可以相应的激活或者关闭Expect:100-Continue握手。
	 * 注意，在HttpClient中，默认是激活的,建议设置为false。
	 */
	private boolean expectContinueEnabled;
	/**是否启用空闲HTTP链接检测,空闲HTTP链接检测会导致每个HTTP	请求有30毫秒的时间损耗,一般建议不要开启*/
    private boolean staleConnectionCheckEnabled;
    /**是否启用自动重定向,默认是开启的,也建议开启*/
    private boolean redirectsEnabled;
    /**是否允许相对链接的自动重定向,HTTP协议规范里规定自动重定向的URL必须是一个绝对URL,默认HttpClient是允许相对链接的自动重定向*/
    private boolean relativeRedirectsAllowed;
    /**环形重定向（重定向到相同路径）是否被允许,建议设置为允许*/
    private boolean circularRedirectsAllowed;
    /**定义重定向的最大数量,若你没有设置此项,HttpClient默认值为50,这里设置默认值为100*/
    private int maxRedirects;
    /**是否开启自动验证(可能你访问某个资源之前,服务器会要求你先进行身份验证,比如用户登录),默认自动验证是开启的*/
    private boolean authenticationEnabled;
    /**定义从HTTP链接池获取一个HTTP链接的超时时间,单位:毫秒,默认值为-1,即表示不限制,0表示无穷大*/
    private int connectionRequestTimeout;
    /**定义建立与服务器的HTTP链接的超时时间,单位:毫秒,默认值为-1,即表示不限制,0表示无穷大*/
    private int connectTimeout;
    /**HTTP请求默认编码UTF-8*/
    private String httpConnectCharset;
    /**连接池每个路由最大链接并发数*/
    private int maxPreRoute;
    /**连接池HTTP请求80端口最大链接并发数*/
    private int maxHttpRoute;
    /**是否开启单Cookie请求头,默认开启,也建议开启*/
    private boolean httpProtocolSingleCookieHeader;
	
	/** 从网页中提取URL链接的最大个数 */
	private int maxOutgoingLinksToFollow;

	/** 网页的最大字节数，超过最大限制的网页将不会被抓取 */
	private long maxDownloadSize;

	/** 是否自动重定向[就好比A写信寄给B,可能B已经搬到C了，即是否自动寄送到C，默认开启自动重定向] */
	private boolean followRedirects;

	/** 是否在线更新顶级域名 */
	private boolean onlineTldListUpdate;

	/** 当队列为空时，是否自动关闭爬虫 */
	private boolean shutdownOnEmptyQueue;
	
	/**是否开启Http GZip压缩*/
	private boolean useGZip;
	
	/**#设置爬虫是否遵守robots协议，默认设置为遵守*/
	private boolean obeyRobotstxt;
	
	/**robots文件默认Map映射key-value键值对最大个数*/
	private int robotsMapMaxSize;
	
	/**设置提取的页面内容的默认编码*/
	private String pageDefaultEncoding;
	
	/**爬虫嵌入式数据库存储目录*/
	private String berkeleyBasepath;
	
	/**爬虫嵌入式数据库名称*/
	private String databaseName;
	
	/**是否开启爬虫嵌入式数据库事务支持,默认开启*/
	private boolean enableTransaction;
	
	/**是否开启爬虫嵌入式数据库的磁盘IO延迟写入功能,默认关闭*/
	private boolean enableDeferWrite;
	
	/**是否允许自动创建数据库,若数据库文件已存在则抛异常,否则自动创建数据库,默认true为允许自动创建*/
	private boolean allowAutoCreateDatabase;
	
	/**是否开启爬虫嵌入式数据库读写锁*/
	private boolean locking;
	
	/**爬虫嵌入式数据库缓存字节数最大值,默认为1024 * 1024 * 4=4M*/
	private long cacheMaxSize;
	
	/**爬虫嵌入式数据库日志文件字节数最大值,默认为1024 * 1024 * 64=64M*/
	private long logMaxSize;
	
	/**爬虫嵌入式数据库数据读写默认编码*/
	private String defaultEncoding;
	
	/**爬虫线程池默认空闲线程个数，默认设置为50个*/
	private int idleThreadOfPool;
	
	/**#爬虫线程池最大线程数量，默认设置为300个*/
	private int maxThreadOfPool;
	
	/**爬虫线程池空闲线程保持存活状态的最大时间，单位：毫秒,默认300000ms*/
	private int idleThreadKeepAliveTime;

	/** 代理主机IP或域名 */
	private String proxyHost;

	/** 代理端口号 */
	private int proxyPort;

	/** 代理帐号 */
	private String proxyUsername;

	/** 代理密码 */
	private String proxyPassword;
	
	/**爬虫Http代理存储路径*/
	private String lastuseProxyDir;

	/** 登录验证信息 */
	private List<AuthInfo> authInfos;
	
	/************************DataBase params*****************************/
	/**数据库类型*/
	private String defaultDatabaseType;
	/**主机名称或者主机IP地址*/
	private String defaultHost;
	/**端口号*/
	private String defaultPort;
	/**数据库名称*/
	private String defaultDatabaseName;
	/**登录帐号*/
	private String defaultUserName;
	/**登录密码*/
	private String defaultPassword;
	
	private CrawlConfig () {
		initialize();
	}
	
	private static class SingletonHolder {  
        private static final CrawlConfig INSTANCE = new CrawlConfig();  
    }  

    public static final CrawlConfig getInstance() {  
        return SingletonHolder.INSTANCE; 
    }  

	/**
	 * @Author Lanxiaowei
	 * @Title: addAuthInfo
	 * @Description: 添加登录验证信息
	 * @param authInfo
	 * @return void
	 * @throws
	 */
	public void addAuthInfo(AuthInfo authInfo) {
		if (this.authInfos == null) {
			this.authInfos = new ArrayList<AuthInfo>();
		}

		this.authInfos.add(authInfo);
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: validate
	 * @Description: 验证爬虫配置信息
	 * @throws Exception
	 * @return void
	 * @throws
	 */
	public void validate() throws Exception {
		
		if (crawlBaseDir == null) {
			throw new Exception("Crawl base directory is not set in the CrawlConfig.");
		}
		if (politenessDelay < 0) {
			throw new Exception("Invalid value for politeness delay: " + politenessDelay);
		}
		if (maxDepthOfCrawling < -1) {
			throw new Exception("Maximum crawl depth should be either a positive number or -1 for unlimited depth.");
		}
		if (maxDepthOfCrawling > Short.MAX_VALUE) {
			throw new Exception("Maximum value for crawl depth is " + Short.MAX_VALUE);
		}
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: setDefaultHeaders
	 * @Description: 设置默认的请求头信息
	 * @param defaultHeaders
	 * @return void
	 * @throws
	 */
	public void setDefaultHeaders(Collection<? extends Header> defaultHeaders) {
		Collection<BasicHeader> copiedHeaders = new HashSet<BasicHeader>();
		for (Header header : defaultHeaders) {
			copiedHeaders.add(new BasicHeader(header.getName(), header.getValue()));
		}
		this.defaultHeaders = copiedHeaders;
	}

	/**
	 * @Author Lanxiaowei
	 * @Title: initialize
	 * @Description: 爬虫配置初始化
	 * @return void
	 * @throws
	 */
	public void initialize() {
		this.crawlBaseDir = Configurations
				.getStringProperty("spider.base_dir", "C:/spider/");
		this.crawlStorageFolder = Configurations
			.getStringProperty("spider.data_store_dir", "C:/spider/data/");
		
		this.crawlResumeFolder = Configurations
			.getStringProperty("spider.data_resume_dir", "C:/spider/temp/");
		
		this.resumableCrawling = Configurations
			.getBooleanProperty("spider.enable_resume", false);

		this.maxDepthOfCrawling = Configurations
			.getIntProperty("spider.max_depth", -1);
		
		/**爬虫抓取网页失败后最大重试次数,默认重试3次*/
		this.maxPageFailurRetryTimes = Configurations
			.getIntProperty("page_failur_retry_times",3);

		this.maxPagesToFetch = Configurations
			.getIntProperty("spider.max_pages_to_fetch", -1);

		this.userAgent = Configurations
			.getStringProperty("spider.user_agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.65 Safari/537.36");

		this.politenessDelay = Configurations
			.getIntProperty("spider.default_politeness_delay", 200);

		this.includeHttpsPages = Configurations
			.getBooleanProperty("spider.support_https", true);

		this.includeBinaryContentInCrawling = Configurations
			.getBooleanProperty("spider.allow_fetch_binary_content", false);

		this.processBinaryContentInCrawling = Configurations
			.getBooleanProperty("spider.process_binary_content_in_crawling", false);

		this.maxConnectionsPerHost = Configurations
			.getIntProperty("http.max_connections_per_host", 100);
		
		this.maxTotalConnections = Configurations
			.getIntProperty("http.max_total_connections", 100);

		this.socketTimeout = Configurations
			.getIntProperty("http.socket_timeout", 20000);

		this.connectionTimeout = Configurations
			.getIntProperty("http.connection_timeout", 30000);
		
		/**立即发送数据,没有TCP延迟*/
		this.socketTcpNoDelay = Configurations
			.getBooleanProperty("socket_tcp_nodelay", true);
		
		/**当调用socket.close()时,是否立即关闭底层的TCP连接,-1表示立即关闭,默认为3600,单位毫秒,即延迟3600毫秒后关闭底层的TCP连接*/
		this.socketCloseDelay = Configurations
			.getLongProperty("socket_close_delay", 3600);
		
		/**是否允许重用Socket所绑定的本地地址*/
		this.socketReuseAddress = Configurations
			.getBooleanProperty("socket_reuse_address", true);
		
		
		/**定义服务器是否愿意接受请求(服务器根据请求头信息判断愿意接受此请求)*/
		this.expectContinueEnabled = Configurations
			.getBooleanProperty("http_expect_continueEnabled", false);
		
		/**是否启用空闲HTTP链接检测,空闲HTTP链接检测会导致每个HTTP	请求有30毫秒的时间损耗,一般建议不要开启*/
		this.staleConnectionCheckEnabled = Configurations
			.getBooleanProperty("http_staleConnectionCheckEnabled", false);
		
	    /**是否启用自动重定向,默认是开启的,也建议开启*/
		this.redirectsEnabled = Configurations
			.getBooleanProperty("http_redirectsEnabled", true);
		
	    /**是否允许相对链接的自动重定向,HTTP协议规范里规定自动重定向的URL必须是一个绝对URL,默认HttpClient是允许相对链接的自动重定向*/
		this.relativeRedirectsAllowed = Configurations
			.getBooleanProperty("http_relativeRedirectsAllowed", true);
		
	    /**环形重定向（重定向到相同路径）是否被允许,建议设置为允许*/
		this.circularRedirectsAllowed = Configurations
			.getBooleanProperty("http_circularRedirectsAllowed", true);
		
	    /**定义重定向的最大数量,若你没有设置此项,HttpClient默认值为50,这里设置默认值为100*/
		this.maxRedirects = Configurations
			.getIntProperty("http_maxRedirects", 100);
		
	    /**是否开启自动验证(可能你访问某个资源之前,服务器会要求你先进行身份验证,比如用户登录),默认自动验证是开启的*/
		this.authenticationEnabled = Configurations
			.getBooleanProperty("http_authenticationEnabled", true);
		
	    /**定义从HTTP链接池获取一个HTTP链接的超时时间,单位:毫秒,默认值为-1,即表示不限制,0表示无穷大*/
		this.connectionRequestTimeout = Configurations
			.getIntProperty("http_connectionRequestTimeout", 5000);
	    /**定义建立与服务器的HTTP链接的超时时间,单位:毫秒,默认值为-1,即表示不限制,0表示无穷大*/
		this.connectTimeout = Configurations
			.getIntProperty("http_connectTimeout", 3000);
		
		
		/**HTTP请求默认编码UTF-8*/
	    this.httpConnectCharset = Configurations
	    	.getStringProperty("http_connect_charset", "UTF-8");
	    /**连接池每个路由最大链接并发数*/
	    this.maxPreRoute = Configurations
			.getIntProperty("max_pre_route", 5000);
	    /**连接池HTTP请求80端口最大链接并发数*/
	    this.maxHttpRoute = Configurations
			.getIntProperty("max_http_route", 500);
	    /**是否开启单Cookie请求头,默认开启,也建议开启*/
		this.httpProtocolSingleCookieHeader = Configurations
			.getBooleanProperty("spider.http.protocol.single-cookie-header", true);

		this.maxOutgoingLinksToFollow = Configurations
			.getIntProperty("spider.max_outlinks_per_page", 5000);
		
		this.maxDownloadSize = Configurations
			.getLongProperty("spider.page_max_size", 1048576);
		
		this.followRedirects = Configurations
			.getBooleanProperty("spider.auto_follow_redirects", true);

		this.onlineTldListUpdate = Configurations
			.getBooleanProperty("spider.online_tld_list_update", true);

		this.shutdownOnEmptyQueue = Configurations
			.getBooleanProperty("spider.shutdown_on_empty_queue", true);
		
		/**是否开启Http GZip压缩*/
		this.useGZip = Configurations
			.getBooleanProperty("spider.use_gzip", false);
		
		/**设置爬虫是否遵守robots协议，默认设置为遵守*/
		this.obeyRobotstxt = Configurations
			.getBooleanProperty("spider.obey_robotstxt", true);
		
		this.robotsMapMaxSize = Configurations
			.getIntProperty("spider.robots_map_max_size", 200);
		
		this.pageDefaultEncoding = Configurations
			.getStringProperty("spider.page_default_encoding", "UTF-8");
		
		/**爬虫线程池默认空闲线程个数，默认设置为50个*/
		this.idleThreadOfPool = Configurations
			.getIntProperty("spider.idle_thread_of_pool", 50);
		
		/**#爬虫线程池最大线程数量，默认设置为300个*/
		this.maxThreadOfPool = Configurations
			.getIntProperty("spider.max_thread_of_pool", 300);
		
		/**爬虫线程池空闲线程保持存活状态的最大时间，单位：毫秒*/
		this.idleThreadKeepAliveTime = Configurations
			.getIntProperty("idle_thread_keep_alive_time", 300000);
		
		/**爬虫嵌入式数据库存储目录*/
		this.berkeleyBasepath = Configurations
			.getStringProperty("db.berkeley_basepath", "C:/berkekeyDB/");
		
		/**爬虫嵌入式数据库名称*/
		this.databaseName = Configurations
			.getStringProperty("db.database_name", "spider4j_db");
		
		/**是否开启爬虫嵌入式数据库事务支持,默认开启*/
		this.enableTransaction = Configurations
			.getBooleanProperty("db.enable_transaction", true);
		
		/**是否开启爬虫嵌入式数据库的磁盘IO延迟写入功能,默认关闭*/
		this.enableDeferWrite = Configurations
			.getBooleanProperty("db.enable_defer_write", false);
		
		/**是否允许自动创建数据库,若数据库文件已存在则抛异常,否则自动创建数据库,默认true为允许自动创建*/
		this.allowAutoCreateDatabase = Configurations
			.getBooleanProperty("db.allow_auto_create_database", true);
		
		/**是否开启爬虫嵌入式数据库读写锁*/
		this.locking = Configurations
			.getBooleanProperty("db.is_locking", true);
		
		/**爬虫嵌入式数据库缓存字节数最大值,默认为1024 * 1024 * 4=4M*/
		this.cacheMaxSize = Configurations
			.getLongProperty("db.cache_max_size", 4194304L);
		
		/**爬虫嵌入式数据库日志文件字节数最大值,默认为1024 * 1024 * 64=64M*/
		this.logMaxSize = Configurations
			.getLongProperty("db.log_max_size", 67108864L);
		
		/**爬虫嵌入式数据库数据读写默认编码*/
		this.defaultEncoding = Configurations
			.getStringProperty("db.default_encoding", "UTF-8");;
		
		//Proxy代理相关参数配置
		this.proxyHost = Configurations
			.getStringProperty("http.proxy_host","xxxxx");

		this.proxyPort = Configurations.getIntProperty("http_proxy_port", 80);

		this.proxyUsername = Configurations
			.getStringProperty("http.proxy_username","xxxxx");

		this.proxyPassword = Configurations
			.getStringProperty("http.proxy_password","xxxxx");
		
		this.lastuseProxyDir = Configurations
			.getStringProperty("spider.lastuse_proxy_dir","C:/spider/data/proxy/lastUse.proxy");
	
		
		/**数据库类型*/
		this.defaultDatabaseType = Configurations
			.getStringProperty("jdbc.default_database_type", "MYSQL");
		
		/**主机名称或者主机IP地址*/
		this.defaultHost = Configurations
			.getStringProperty("jdbc.default_host", "localhost");
		
		/**端口号*/
		this.defaultPort = Configurations
			.getStringProperty("jdbc.default_port", "3306");
		
		/**数据库名称*/
		this.defaultDatabaseName = Configurations
			.getStringProperty("jdbc.default_database_name", "test");
		
		/**登录帐号*/
		this.defaultUserName = Configurations
			.getStringProperty("jdbc.default_user_name", "root");
		
		/**登录密码*/
		this.defaultPassword = Configurations
			.getStringProperty("jdbc.default_password", "123");
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Crawl storage folder: " + getCrawlStorageFolder() + "\n");
		sb.append("Resumable crawling: " + isResumableCrawling() + "\n");
		sb.append("Max depth of crawl: " + getMaxDepthOfCrawling() + "\n");
		sb.append("Max pages to fetch: " + getMaxPagesToFetch() + "\n");
		sb.append("User agent string: " + getUserAgent() + "\n");
		sb.append("Include https pages: " + isIncludeHttpsPages() + "\n");
		sb.append("Include binary content: " + isIncludeBinaryContentInCrawling() + "\n");
		sb.append("Max connections per host: " + getMaxConnectionsPerHost() + "\n");
		sb.append("Max total connections: " + getMaxTotalConnections() + "\n");
		sb.append("Socket timeout: " + getSocketTimeout() + "\n");
		sb.append("Max total connections: " + getMaxTotalConnections() + "\n");
		sb.append("Max outgoing links to follow: " + getMaxOutgoingLinksToFollow() + "\n");
		sb.append("Max download size: " + getMaxDownloadSize() + "\n");
		sb.append("Should follow redirects?: " + isFollowRedirects() + "\n");
		sb.append("use http GZip?: " + isUseGZip() + "\n");
		sb.append("Proxy host: " + getProxyHost() + "\n");
		sb.append("Proxy port: " + getProxyPort() + "\n");
		sb.append("Proxy username: " + getProxyUsername() + "\n");
		sb.append("Proxy password: " + getProxyPassword() + "\n");
		return sb.toString();
	}

	public String getCrawlStorageFolder() {
		return crawlStorageFolder;
	}

	public void setCrawlStorageFolder(String crawlStorageFolder) {
		this.crawlStorageFolder = crawlStorageFolder;
	}

	public boolean isResumableCrawling() {
		return resumableCrawling;
	}

	public void setResumableCrawling(boolean resumableCrawling) {
		this.resumableCrawling = resumableCrawling;
	}

	public int getMaxDepthOfCrawling() {
		return maxDepthOfCrawling;
	}

	public void setMaxDepthOfCrawling(int maxDepthOfCrawling) {
		this.maxDepthOfCrawling = maxDepthOfCrawling;
	}

	public int getMaxPageFailurRetryTimes() {
		return maxPageFailurRetryTimes;
	}

	public void setMaxPageFailurRetryTimes(int maxPageFailurRetryTimes) {
		this.maxPageFailurRetryTimes = maxPageFailurRetryTimes;
	}

	public int getMaxPagesToFetch() {
		return maxPagesToFetch;
	}

	public void setMaxPagesToFetch(int maxPagesToFetch) {
		this.maxPagesToFetch = maxPagesToFetch;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Collection<BasicHeader> getDefaultHeaders() {
		return defaultHeaders;
	}

	public int getPolitenessDelay() {
		return politenessDelay;
	}

	public void setPolitenessDelay(int politenessDelay) {
		this.politenessDelay = politenessDelay;
	}

	public boolean isIncludeHttpsPages() {
		return includeHttpsPages;
	}

	public void setIncludeHttpsPages(boolean includeHttpsPages) {
		this.includeHttpsPages = includeHttpsPages;
	}

	public boolean isIncludeBinaryContentInCrawling() {
		return includeBinaryContentInCrawling;
	}

	public void setIncludeBinaryContentInCrawling(
			boolean includeBinaryContentInCrawling) {
		this.includeBinaryContentInCrawling = includeBinaryContentInCrawling;
	}

	public boolean isProcessBinaryContentInCrawling() {
		return processBinaryContentInCrawling;
	}

	public void setProcessBinaryContentInCrawling(
			boolean processBinaryContentInCrawling) {
		this.processBinaryContentInCrawling = processBinaryContentInCrawling;
	}

	public int getMaxConnectionsPerHost() {
		return maxConnectionsPerHost;
	}

	public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
		this.maxConnectionsPerHost = maxConnectionsPerHost;
	}

	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getMaxOutgoingLinksToFollow() {
		return maxOutgoingLinksToFollow;
	}

	public void setMaxOutgoingLinksToFollow(int maxOutgoingLinksToFollow) {
		this.maxOutgoingLinksToFollow = maxOutgoingLinksToFollow;
	}

	public long getMaxDownloadSize() {
		return maxDownloadSize;
	}

	public void setMaxDownloadSize(long maxDownloadSize) {
		this.maxDownloadSize = maxDownloadSize;
	}

	public boolean isFollowRedirects() {
		return followRedirects;
	}

	public void setFollowRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
	}

	public boolean isOnlineTldListUpdate() {
		return onlineTldListUpdate;
	}

	public void setOnlineTldListUpdate(boolean onlineTldListUpdate) {
		this.onlineTldListUpdate = onlineTldListUpdate;
	}

	public boolean isShutdownOnEmptyQueue() {
		return shutdownOnEmptyQueue;
	}

	public void setShutdownOnEmptyQueue(boolean shutdownOnEmptyQueue) {
		this.shutdownOnEmptyQueue = shutdownOnEmptyQueue;
	}

	public boolean isUseGZip() {
		return useGZip;
	}

	public void setUseGZip(boolean useGZip) {
		this.useGZip = useGZip;
	}

	public boolean isObeyRobotstxt() {
		return obeyRobotstxt;
	}

	public void setObeyRobotstxt(boolean obeyRobotstxt) {
		this.obeyRobotstxt = obeyRobotstxt;
	}

	public int getRobotsMapMaxSize() {
		return robotsMapMaxSize;
	}

	public void setRobotsMapMaxSize(int robotsMapMaxSize) {
		this.robotsMapMaxSize = robotsMapMaxSize;
	}

	public String getPageDefaultEncoding() {
		return pageDefaultEncoding;
	}

	public void setPageDefaultEncoding(String pageDefaultEncoding) {
		this.pageDefaultEncoding = pageDefaultEncoding;
	}

	public String getBerkeleyBasepath() {
		return berkeleyBasepath;
	}

	public void setBerkeleyBasepath(String berkeleyBasepath) {
		this.berkeleyBasepath = berkeleyBasepath;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public boolean isEnableTransaction() {
		return enableTransaction;
	}

	public void setEnableTransaction(boolean enableTransaction) {
		this.enableTransaction = enableTransaction;
	}

	public boolean isEnableDeferWrite() {
		return enableDeferWrite;
	}

	public void setEnableDeferWrite(boolean enableDeferWrite) {
		this.enableDeferWrite = enableDeferWrite;
	}

	public boolean isAllowAutoCreateDatabase() {
		return allowAutoCreateDatabase;
	}

	public void setAllowAutoCreateDatabase(boolean allowAutoCreateDatabase) {
		this.allowAutoCreateDatabase = allowAutoCreateDatabase;
	}

	public boolean isLocking() {
		return locking;
	}

	public void setLocking(boolean locking) {
		this.locking = locking;
	}

	public long getCacheMaxSize() {
		return cacheMaxSize;
	}

	public void setCacheMaxSize(long cacheMaxSize) {
		this.cacheMaxSize = cacheMaxSize;
	}

	public long getLogMaxSize() {
		return logMaxSize;
	}

	public void setLogMaxSize(long logMaxSize) {
		this.logMaxSize = logMaxSize;
	}

	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	public int getIdleThreadOfPool() {
		return idleThreadOfPool;
	}

	public void setIdleThreadOfPool(int idleThreadOfPool) {
		this.idleThreadOfPool = idleThreadOfPool;
	}

	public int getMaxThreadOfPool() {
		return maxThreadOfPool;
	}

	public void setMaxThreadOfPool(int maxThreadOfPool) {
		this.maxThreadOfPool = maxThreadOfPool;
	}

	public int getIdleThreadKeepAliveTime() {
		return idleThreadKeepAliveTime;
	}

	public void setIdleThreadKeepAliveTime(int idleThreadKeepAliveTime) {
		this.idleThreadKeepAliveTime = idleThreadKeepAliveTime;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUsername() {
		return proxyUsername;
	}

	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public String getLastuseProxyDir() {
		return lastuseProxyDir;
	}

	public void setLastuseProxyDir(String lastuseProxyDir) {
		this.lastuseProxyDir = lastuseProxyDir;
	}

	public List<AuthInfo> getAuthInfos() {
		return authInfos;
	}

	public void setAuthInfos(List<AuthInfo> authInfos) {
		this.authInfos = authInfos;
	}

	public boolean isSocketTcpNoDelay() {
		return socketTcpNoDelay;
	}

	public void setSocketTcpNoDelay(boolean socketTcpNoDelay) {
		this.socketTcpNoDelay = socketTcpNoDelay;
	}

	public long getSocketCloseDelay() {
		return socketCloseDelay;
	}

	public void setSocketCloseDelay(long socketCloseDelay) {
		this.socketCloseDelay = socketCloseDelay;
	}

	public boolean isSocketReuseAddress() {
		return socketReuseAddress;
	}

	public void setSocketReuseAddress(boolean socketReuseAddress) {
		this.socketReuseAddress = socketReuseAddress;
	}

	public boolean isExpectContinueEnabled() {
		return expectContinueEnabled;
	}

	public void setExpectContinueEnabled(boolean expectContinueEnabled) {
		this.expectContinueEnabled = expectContinueEnabled;
	}

	public boolean isStaleConnectionCheckEnabled() {
		return staleConnectionCheckEnabled;
	}

	public void setStaleConnectionCheckEnabled(boolean staleConnectionCheckEnabled) {
		this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
	}

	public boolean isRedirectsEnabled() {
		return redirectsEnabled;
	}

	public void setRedirectsEnabled(boolean redirectsEnabled) {
		this.redirectsEnabled = redirectsEnabled;
	}

	public boolean isRelativeRedirectsAllowed() {
		return relativeRedirectsAllowed;
	}

	public void setRelativeRedirectsAllowed(boolean relativeRedirectsAllowed) {
		this.relativeRedirectsAllowed = relativeRedirectsAllowed;
	}

	public boolean isCircularRedirectsAllowed() {
		return circularRedirectsAllowed;
	}

	public void setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
		this.circularRedirectsAllowed = circularRedirectsAllowed;
	}

	public int getMaxRedirects() {
		return maxRedirects;
	}

	public void setMaxRedirects(int maxRedirects) {
		this.maxRedirects = maxRedirects;
	}

	public boolean isAuthenticationEnabled() {
		return authenticationEnabled;
	}

	public void setAuthenticationEnabled(boolean authenticationEnabled) {
		this.authenticationEnabled = authenticationEnabled;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public String getCrawlBaseDir() {
		return crawlBaseDir;
	}

	public void setCrawlBaseDir(String crawlBaseDir) {
		this.crawlBaseDir = crawlBaseDir;
	}

	public String getCrawlResumeFolder() {
		return crawlResumeFolder;
	}

	public void setCrawlResumeFolder(String crawlResumeFolder) {
		this.crawlResumeFolder = crawlResumeFolder;
	}

	public String getDefaultDatabaseType() {
		return defaultDatabaseType;
	}

	public void setDefaultDatabaseType(String defaultDatabaseType) {
		this.defaultDatabaseType = defaultDatabaseType;
	}

	public String getDefaultHost() {
		return defaultHost;
	}

	public void setDefaultHost(String defaultHost) {
		this.defaultHost = defaultHost;
	}

	public String getDefaultPort() {
		return defaultPort;
	}

	public void setDefaultPort(String defaultPort) {
		this.defaultPort = defaultPort;
	}

	public String getDefaultDatabaseName() {
		return defaultDatabaseName;
	}

	public void setDefaultDatabaseName(String defaultDatabaseName) {
		this.defaultDatabaseName = defaultDatabaseName;
	}

	public String getDefaultUserName() {
		return defaultUserName;
	}

	public void setDefaultUserName(String defaultUserName) {
		this.defaultUserName = defaultUserName;
	}

	public String getDefaultPassword() {
		return defaultPassword;
	}

	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}

	public String getHttpConnectCharset() {
		return httpConnectCharset;
	}

	public void setHttpConnectCharset(String httpConnectCharset) {
		this.httpConnectCharset = httpConnectCharset;
	}

	public int getMaxPreRoute() {
		return maxPreRoute;
	}

	public void setMaxPreRoute(int maxPreRoute) {
		this.maxPreRoute = maxPreRoute;
	}

	public int getMaxHttpRoute() {
		return maxHttpRoute;
	}

	public void setMaxHttpRoute(int maxHttpRoute) {
		this.maxHttpRoute = maxHttpRoute;
	}

	public boolean isHttpProtocolSingleCookieHeader() {
		return httpProtocolSingleCookieHeader;
	}

	public void setHttpProtocolSingleCookieHeader(
			boolean httpProtocolSingleCookieHeader) {
		this.httpProtocolSingleCookieHeader = httpProtocolSingleCookieHeader;
	}
}
