package com.yida.spider4j.crawler.core;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.HttpHost;

import com.yida.spider4j.crawler.auth.login.Login;
import com.yida.spider4j.crawler.auth.login.LoginFailurelException;
import com.yida.spider4j.crawler.auth.param.AuthInfo;
import com.yida.spider4j.crawler.auth.param.Feedback;
import com.yida.spider4j.crawler.download.Downloader;
import com.yida.spider4j.crawler.download.HttpClientDownloader;
import com.yida.spider4j.crawler.listener.SpiderListener;
import com.yida.spider4j.crawler.pipeline.CollectorPipeline;
import com.yida.spider4j.crawler.pipeline.ConsolePipeline;
import com.yida.spider4j.crawler.pipeline.PageResultItemCollectorPipeline;
import com.yida.spider4j.crawler.pipeline.Pipeline;
import com.yida.spider4j.crawler.processor.SimpleDetailPageProcessor;
import com.yida.spider4j.crawler.processor.SimpleListPageProcessor;
import com.yida.spider4j.crawler.processor.SimpleSeedPageProcessor;
import com.yida.spider4j.crawler.processor.SimpleStartPageProcessor;
import com.yida.spider4j.crawler.scheduler.QueueScheduler;
import com.yida.spider4j.crawler.scheduler.Scheduler;
import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.thread.ThreadPool;
import com.yida.spider4j.crawler.utils.collection.Lists;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.log.LogUtils;
import com.yida.spider4j.crawler.utils.url.URLUtils;

/**
 * @ClassName: Spider
 * @Description: 爬虫入口类
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午1:26:02
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class Spider implements Runnable, Task {
	/**网页下载器*/
    protected Downloader downloader;

    /**数据消费管道*/
    protected List<Pipeline> pipelines = new ArrayList<Pipeline>();

    /**种子页网页数据提取器*/
    protected SimpleSeedPageProcessor seedPageProcessor;
    
    /**起始页网页数据提取器*/
    protected SimpleStartPageProcessor startPageProcessor;
    
    /**列表页网页数据提取器*/
    protected SimpleListPageProcessor listPageProcessor;
    
    /**详情页网页数据提取器*/
    protected SimpleDetailPageProcessor detailPageProcessor;

    /**起始请求Request集合,由起始URL包装得到*/
    protected List<Request> startRequests;
    
    /**起始URL[当起始URL有多个时需要指定]*/
    protected List<String> startUrls;
    
    /**起始URL*/
    protected String startUrl;
    
    /**起始页请求头信息*/
    protected Map<String,String> headers;
    
    /**起始页POST请求参数*/
    protected Map<String,String> params;

    /**待抓取的目标网站*/
    protected Site site;

    /**爬虫的唯一标识值*/
    protected String uuid;

    /**URL调度器*/
    protected Scheduler scheduler = new QueueScheduler();
    
    /**计数器*/
	private CountDownLatch countDownLatch;
	/**互斥锁*/
	private Lock lock;

    protected ThreadPool threadPool;

    /**线程池管理器[JDK自带的]*/
    protected ExecutorService executorService;

    /**默认爬虫实例只开启一个线程来爬取*/
    protected int threadNum = 1;

    /**爬虫状态值*/
    protected AtomicInteger stat = new AtomicInteger(STAT_INIT);

    /**所有任务完成后是否爬虫退出*/
    protected boolean exitWhenComplete = true;

    /***************爬虫运行状态:初始化/运行中/已停止***************/
    protected final static int STAT_INIT = 0;

    protected final static int STAT_RUNNING = 1;

    protected final static int STAT_STOPPED = 2;
    /***************爬虫运行状态:end***************/

    /****************是否展开URL,false表示只爬取startUrl这一个网页**************/
    protected boolean spawnUrl = true;

    protected boolean destroyWhenExit = true;

    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();

    private List<SpiderListener> spiderListeners;

    private final AtomicLong pageCount = new AtomicLong(0);

    private Date startTime;

    private int emptySleepTime = 30000;
    
    /**登录验证器*/
    private Login login;
    
    /**登录验证返回结果*/
    private Feedback feedback;
    
    /**
     * 设置爬虫实例执行次数,默认只执行1次,执行完就结束了
     * 1. invokeTime <= 0 --> 则认为需要一直执行,不需要停止
     * 2. invokeTime = 1  --> 默认值,只执行1次,你懂的
     * 3. invokeTime > 1  --> 表示需要执行的特定次数,执行完指定次数后爬虫最终会停止
     */
    private int invokeTime;
    
    private static final int DEFAULT_INVOKE_TIME = 1;
    
    public static Spider create(SimpleSeedPageProcessor seedPageProcessor,
    		SimpleStartPageProcessor startPageProcessor,
    		SimpleListPageProcessor listPageProcessor,
    		SimpleDetailPageProcessor detailPageProcessor) {
        return new Spider(seedPageProcessor,startPageProcessor,listPageProcessor,detailPageProcessor);
    }

    public static Spider create(SimpleStartPageProcessor startPageProcessor,
    		SimpleListPageProcessor listPageProcessor,
    		SimpleDetailPageProcessor detailPageProcessor) {
        return new Spider(startPageProcessor,listPageProcessor,detailPageProcessor);
    }
    
    public static Spider create(SimpleListPageProcessor listPageProcessor,
    		SimpleDetailPageProcessor detailPageProcessor) {
        return new Spider(listPageProcessor,detailPageProcessor);
    }
    
    public Spider(SimpleSeedPageProcessor seedPageProcessor,
    		SimpleStartPageProcessor startPageProcessor,
    		SimpleListPageProcessor listPageProcessor,
    		SimpleDetailPageProcessor detailPageProcessor,int invokeTime) {
    	if(startPageProcessor == null && listPageProcessor == null && 
    		detailPageProcessor == null) {
    		throw new IllegalArgumentException("SimpleStartPageProcessor,SimpleListPageProcessor and SimpleDetailPageProcessor MUST not be all null.");
    	}
    	//设置爬虫实例执行次数
    	this.invokeTime = invokeTime;
    	this.lock = new ReentrantLock();
    	if(invokeTime > 0) {
    		this.countDownLatch = new CountDownLatch(invokeTime);
    	}
    	
    	this.seedPageProcessor = seedPageProcessor;
        this.startPageProcessor = startPageProcessor;
        this.listPageProcessor = listPageProcessor;
        this.detailPageProcessor = detailPageProcessor;
        this.site = detailPageProcessor.getSite();
        if(startPageProcessor != null && 
        	startPageProcessor.getSite() != null) {
        	this.startRequests = startPageProcessor.getSite().getStartRequests();
        	this.startUrls = startPageProcessor.getSite().getStartUrls();
        	this.startUrl = startPageProcessor.getSite().getStartUrl();
        } else if (null != this.site){
        	this.startRequests = this.site.getStartRequests();
        	this.startUrls = this.site.getStartUrls();
        	this.startUrl = this.site.getStartUrl();
        	
        } else {
        	throw new IllegalArgumentException("You don't still set the startUrl.");
        }
    }

    public Spider(SimpleSeedPageProcessor seedPageProcessor,
    		SimpleStartPageProcessor startPageProcessor,
    		SimpleListPageProcessor listPageProcessor,
    		SimpleDetailPageProcessor detailPageProcessor) {
    	//默认爬虫实例只会执行一次
    	this(seedPageProcessor, startPageProcessor, listPageProcessor, detailPageProcessor, DEFAULT_INVOKE_TIME);
    }
    
    public Spider(SimpleStartPageProcessor startPageProcessor,
    		SimpleListPageProcessor listPageProcessor,
    		SimpleDetailPageProcessor detailPageProcessor) {
    	this(null,startPageProcessor, listPageProcessor, detailPageProcessor);
    }
    
    public Spider(SimpleListPageProcessor listPageProcessor,
    		SimpleDetailPageProcessor detailPageProcessor) {
        this(null,null,listPageProcessor, detailPageProcessor);
    }

    public Spider startUrls(List<String> startUrls) {
        checkIfRunning();
        this.startRequests = Site.urlToRequests(startUrls);
        return this;
    }

    public Spider startRequests(List<Request> startRequests) {
        checkIfRunning();
        this.startRequests = startRequests;
        return this;
    }

    public Spider setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    /**设置爬虫的URL调度器*/
    public Spider scheduler(Scheduler scheduler) {
        return setScheduler(scheduler);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setScheduler
     * @Description: 设置爬虫的URL调度器
     * @param @param scheduler
     * @param @return
     * @return Spider
     * @throws
     */
    public Spider setScheduler(Scheduler scheduler) {
        checkIfRunning();
        Scheduler oldScheduler = this.scheduler;
        this.scheduler = scheduler;
        if (oldScheduler != null) {
            Request request = null;
            while ((request = oldScheduler.poll(this)) != null) {
                this.scheduler.push(request, this);
            }
        }
        return this;
    }

    /**添加一个管道*/
    public Spider pipeline(Pipeline pipeline) {
        return addPipeline(pipeline);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: addPipeline
     * @Description: 为爬虫添加一个管道
     * @param @param pipeline
     * @param @return
     * @return Spider
     * @throws
     */
    public Spider addPipeline(Pipeline pipeline) {
        checkIfRunning();
        this.pipelines.add(pipeline);
        return this;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setPipelines
     * @Description: 添加多个管道
     * @param @param pipelines
     * @param @return
     * @return Spider
     * @throws
     */
    public Spider setPipelines(List<Pipeline> pipelines) {
        checkIfRunning();
        this.pipelines = pipelines;
        return this;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: clearPipeline
     * @Description: 清空所有管道设置
     * @param @return
     * @return Spider
     * @throws
     */
    public Spider clearPipeline() {
        pipelines = null;
        return this;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: downloader
     * @Description: 设置一个网页下载器
     * @param @param downloader
     * @param @return
     * @return Spider
     * @throws
     */
    public Spider downloader(Downloader downloader) {
        return setDownloader(downloader);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setDownloader
     * @Description: 设置一个网页下载器
     * @param @param downloader
     * @param @return
     * @return Spider
     * @throws
     */
    public Spider setDownloader(Downloader downloader) {
        checkIfRunning();
        this.downloader = downloader;
        return this;
    }

    public SimpleSeedPageProcessor getSeedPageProcessor() {
		return seedPageProcessor;
	}

	public Spider setSeedPageProcessor(SimpleSeedPageProcessor seedPageProcessor) {
		this.seedPageProcessor = seedPageProcessor;
		return this;
	}

	public SimpleStartPageProcessor getStartPageProcessor() {
		return startPageProcessor;
	}

	public Spider setStartPageProcessor(SimpleStartPageProcessor startPageProcessor) {
		this.startPageProcessor = startPageProcessor;
		return this;
	}

	public SimpleListPageProcessor getListPageProcessor() {
		return listPageProcessor;
	}

	public Spider setListPageProcessor(SimpleListPageProcessor listPageProcessor) {
		this.listPageProcessor = listPageProcessor;
		return this;
	}

	public SimpleDetailPageProcessor getDetailPageProcessor() {
		return detailPageProcessor;
	}

	public Spider setDetailPageProcessor(SimpleDetailPageProcessor detailPageProcessor) {
		this.detailPageProcessor = detailPageProcessor;
		return this;
	}

	/**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: initComponent
     * @Description: Spider初始化
     * @param 
     * @return void
     * @throws
     */
    protected void initComponent() {
        if (downloader == null) {
            this.downloader = new HttpClientDownloader();
        }
        if(pipelines == null) {
        	pipelines = new ArrayList<Pipeline>();
        }
        if (pipelines.isEmpty()) {
            pipelines.add(new ConsolePipeline());
        }
        //设置开启的线程数
        downloader.setThreadNum(threadNum);
        if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new ThreadPool(threadNum, executorService);
            } else {
                threadPool = new ThreadPool(threadNum);
            }
        }
        /*if (startRequests != null && startRequests.size() > 0) {
            for (Request request : startRequests) {
                scheduler.push(request, this);
            }
            startRequests.clear();
        }else if(startUrls != null && startUrls.size() > 0) {
        	for (String url : startUrls) {
        		//默认采用GET请求,否则请使用Request对象构造你的请求:request.setMothod(HttpConstant.Method.POST);
        		Request request = new Request(url, PageType.LIST_PAGE,HttpConstant.Method.GET);
                scheduler.push(request, this);
            }
        	startUrls.clear();
        } else if(StringUtils.isNotEmpty(startUrl)){
        	if(this.startPageProcessor == null) {
        		Request request = new Request(startUrl, PageType.LIST_PAGE,HttpConstant.Method.GET);
                scheduler.push(request, this);
        	} else {
        		Request request = new Request(startUrl, PageType.START_PAGE,HttpConstant.Method.GET);
                scheduler.push(request, this);
        	}
        } else {
        	throw new IllegalArgumentException("one of startUrl & startUrls & startRequests MUST not be null.");
        }*/
        startTime = new Date();
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: preLogin
     * @Description: 抓取之前,先进行登录验证
     * @param 
     * @return void
     * @throws
     */
    public void preLogin() {
    	//若没有设置登录验证器,则直接跳过
    	if(this.login == null) {
    		return;
    	}
    	AuthInfo authInfo = this.login.buildAuthInfo();
    	//缓存下登录验证的回馈结果
    	this.feedback = this.login.doAuthenticate(authInfo);
    }

    @Override
    public void run() {
    	if(this.login != null && this.feedback != null) {
    		boolean loginSuccess = this.feedback.isLoginSuccess();
    		//若登录验证不通过,则直接抛异常终止抓取,提示用户
    		if(!loginSuccess) {
    			try {
					throw new LoginFailurelException("Spider has auto login authenticate,but return failure.");
				} catch (LoginFailurelException e) {
					e.printStackTrace();
				}
    			return;
    		}
    	}
    	if(invokeTime <= 0) {
    		while(true) {
    			work();
    		}
    	} else {
    		for(int i=0; i < invokeTime; i++) {
    			work();
    		}
    	}
    	try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
    }
    
    private void work() {
    	try {
    		//关门上锁,我没执行完之前,其他爬虫实例就乖乖等着(保证多个爬虫实例按顺序串行执行)
        	lock.lock();
            checkRunningStat();
            initComponent();
            LogUtils.info("Spider " + taskId() + " started!");
            while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
                Request request = scheduler.poll(this);
                if (request == null) {
                    if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
                        break;
                    }
                    // 阻塞等待新的URL添加到队列中
                    waitNewUrl();
                } else {
                    final Request requestFinal = request;
                    threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                processRequest(requestFinal);
                                onSuccess(requestFinal);
                            } catch (Exception e) {
                                onError(requestFinal);
                                LogUtils.warn("process request " + requestFinal + " error:\n" + e.getMessage());
                            } finally {
                                if (site.getHttpProxyPool()!=null && site.getHttpProxyPool().isEnable()) {
                                    site.returnHttpProxyToPool((HttpHost) requestFinal.getExtra(Request.PROXY), (Integer) requestFinal
                                            .getExtra(Request.STATUS_CODE));
                                }
                                pageCount.incrementAndGet();
                                signalNewUrl();
                            }
                        }
                    });
                }
            }
            stat.set(STAT_STOPPED);
    	} finally {
    		if(null != countDownLatch) {
    			countDownLatch.countDown();
    		}
			//释放锁(我执行完了,其他爬虫实例进来吧,轮到你们啦,哈哈!!!)
			lock.unlock();
			
			// 释放资源
	        if (destroyWhenExit) {
	            close();
	        }
		}
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: onError
     * @Description: 爬虫爬取网页失败时的回调函数
     * @param @param request
     * @return void
     * @throws
     */
    protected void onError(Request request) {
        if (null != spiderListeners && spiderListeners.size() > 0) {
            for (SpiderListener spiderListener : spiderListeners) {
                spiderListener.onError(request);
            }
        }
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: onSuccess
     * @Description: 爬虫爬取网页成功时的回调函数
     * @param @param request
     * @return void
     * @throws
     */
    protected void onSuccess(Request request) {
    	if (null != spiderListeners && spiderListeners.size() > 0) {
            for (SpiderListener spiderListener : spiderListeners) {
                spiderListener.onSuccess(request);
            }
        }
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: checkRunningStat
     * @Description: 检查爬虫的运行状态
     * @param 
     * @return void
     * @throws
     */
    private void checkRunningStat() {
        while (true) {
            int statNow = stat.get();
            if (statNow == STAT_RUNNING) {
                throw new IllegalStateException("Spider is already running!");
            }
            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
                break;
            }
        }
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: close
     * @Description: 释放资源
     * @param 
     * @return void
     * @throws
     */
    public void close() {
        destroyEach(downloader);
        destroyEach(startPageProcessor);
        destroyEach(listPageProcessor);
        destroyEach(detailPageProcessor);
        
        if(pipelines != null && pipelines.size() > 0) {
        	for (Pipeline pipeline : pipelines) {
                destroyEach(pipeline);
            }
        }
        threadPool.shutdown();
    }

    private void destroyEach(Object object) {
        if(object == null) {
        	return;
        }
        if (object instanceof Closeable) {
            try {
                ((Closeable) object).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void test(String... urls) {
        initComponent();
        if (urls.length > 0) {
            for (String url : urls) {
                processRequest(new Request(url));
            }
        }
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: processRequest
     * @Description: 处理每个Request
     * @param @param request
     * @return void
     * @throws
     */
    protected void processRequest(Request request) {
    	//下载网页
        Page page = downloader.download(request, this);
        //如果网页下载失败
        if (page == null) {
            sleep(site.getRetrySleepTime());
            onError(request);
            return;
        }
        //是否需要周期性不间断抓取
        if (page.isNeedCycleRetry()) {
            extractAndAddRequests(page, true);
            sleep(site.getRetrySleepTime());
            return;
        }
        if(page.getPageType().equals(PageType.SEED_PAGE)) {
        	seedPageProcessor.buildRequest(page);
        } else if(page.getPageType().equals(PageType.SEED_PAGING_PAGE)) {
        	seedPageProcessor.buildRequest(page);
        } else if(page.getPageType().equals(PageType.START_PAGE)) {
        	startPageProcessor.buildRequest(page);
        } else if(page.getPageType().equals(PageType.LIST_PAGE)) {
        	listPageProcessor.buildRequest(page);
        } else if(page.getPageType().equals(PageType.DETAIL_PAGE)) {
        	detailPageProcessor.process(page);
        }
        extractAndAddRequests(page, spawnUrl);
        if (null != page.getPageResultItem() &&
        	!page.getPageResultItem().isSkip()) {
        	if(page.getPageResultItem().getDataMap() != null &&
            		!page.getPageResultItem().getDataMap().isEmpty()) {
        		doPipeline(page);
        	} else if(page.getPageResultItem().getDataMapList() != null &&
        			page.getPageResultItem().getDataMapList().size() > 0) {
        		doPipeline(page);
        	}
        }
        
        request.putExtra(Request.STATUS_CODE, page.getStatusCode());
        sleep(site.getSleepTime());
    }

	private void doPipeline(Page page) {
		for (Pipeline pipeline : pipelines) {
		    pipeline.process(page.getPageResultItem(), this);
		}
	}

    protected void sleep(int time) {
    	if(time <= 0) {
    		return;
    	}
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void extractAndAddRequests(Page page, boolean spawnUrl) {
        if (spawnUrl && (null != page.getTargetRequests() && page.getTargetRequests().size() > 0)) {
            for (Request request : page.getTargetRequests()) {
                addRequest(request);
            }
        }
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: addRequest
     * @Description: 将Request放入URL调度器
     * @param @param request
     * @return void
     * @throws
     */
    private void addRequest(Request request) {
        if (site.getDomain() == null && request != null && request.getUrl() != null) {
            site.setDomain(URLUtils.getDomain(request.getUrl()));
        }
        scheduler.push(request, this);
    }

    protected void checkIfRunning() {
        if (stat.get() == STAT_RUNNING) {
            throw new IllegalStateException("Spider is already running!");
        }
    }

    public void runAsync() {
        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.start();
    }

    /**添加URL到URL队列*/
    public Spider addUrl(String... urls) {
        for (String url : urls) {
            addRequest(new Request(url,PageType.DETAIL_PAGE));
        }
        signalNewUrl();
        if(this.site != null) {
        	site.setStartUrls(Arrays.asList(urls));
        }
        return this;
    }
    
    public Spider addUrls(String[] urls,PageType pageType) {
    	for (String url : urls) {
            addRequest(new Request(url,pageType));
        }
        signalNewUrl();
        if(this.site != null) {
        	site.setStartUrls(Arrays.asList(urls));
        }
        return this;
    }
    
    public Spider addUrl(String url,PageType pageType) {
    	addRequest(new Request(url,pageType));
        signalNewUrl();
        if(this.site != null) {
        	site.setStartUrl(url);
        }
        return this;
    }
    
    public Spider startUrl(String url,PageType pageType,String method) {
    	Request request = new Request(url,pageType,method);
    	initRequest(request);
    	addRequest(request);
        signalNewUrl();
        if(this.site != null) {
        	site.setStartUrl(url);
        } 
        if(StringUtils.isEmpty(this.startUrl)) {
        	this.startUrl = url;
        }
        return this;
    }
    
    public Spider startUrl(String url,String method) {
    	Request request = new Request(url,PageType.START_PAGE,method);
    	initRequest(request);
    	addRequest(request);
        signalNewUrl();
        if(this.site != null) {
        	site.setStartUrl(url);
        } 
        if(StringUtils.isEmpty(this.startUrl)) {
        	this.startUrl = url;
        }
        return this;
    }
    
    public Spider startUrl(String url,PageType pageType) {
    	Request request = new Request(url,pageType);
    	initRequest(request);
    	addRequest(request);
        signalNewUrl();
        if(this.site != null) {
        	site.setStartUrl(url);
        } 
        if(StringUtils.isEmpty(this.startUrl)) {
        	this.startUrl = url;
        }
        return this;
    }
    
    public Spider startUrl(String url) {
    	Request request = new Request(url,PageType.START_PAGE);
    	initRequest(request);
    	addRequest(request);
        signalNewUrl();
        if(this.site != null) {
        	site.setStartUrl(url);
        } 
        if(StringUtils.isEmpty(this.startUrl)) {
        	this.startUrl = url;
        }
        return this;
    }
    
    public Spider startUrls(String[] urls,PageType pageType) {
    	for (String url : urls) {
    		Request request = new Request(url,pageType);
        	initRequest(request);
        	addRequest(request);
        }
        signalNewUrl();
        if(this.site != null) {
        	site.setStartUrls(Arrays.asList(urls));
        } 
        if(this.startUrls == null) {
        	this.startUrls = Arrays.asList(urls);
        } else {
        	this.startUrls.addAll(Arrays.asList(urls));
        }
        return this;
    }
    
    public Spider startUrls(String[] urls) {
    	for (String url : urls) {
    		Request request = new Request(url,PageType.START_PAGE);
        	initRequest(request);
        	addRequest(request);
        }
        signalNewUrl();
        if(this.site != null) {
        	site.setStartUrls(Arrays.asList(urls));
        } 
        if(this.startUrls == null) {
        	this.startUrls = Arrays.asList(urls);
        } else {
        	this.startUrls.addAll(Arrays.asList(urls));
        }
        return this;
    }

    /**
     * Download urls synchronizing.
     *
     * @param urls
     * @return
     */
	public <T> List<T> getAll(Collection<String> urls) {
        destroyWhenExit = false;
        spawnUrl = false;
        startRequests.clear();
        for (Request request : Site.urlToRequests(urls)) {
            addRequest(request);
        }
        
		CollectorPipeline collectorPipeline = getCollectorPipeline();
        pipelines.add(collectorPipeline);
        run();
        spawnUrl = true;
        destroyWhenExit = true;
        return collectorPipeline.getCollected();
    }

    protected CollectorPipeline getCollectorPipeline() {
        return new PageResultItemCollectorPipeline();
    }

    public <T> T get(String url) {
        List<String> urls = Lists.newArrayList(url);
        List<T> resultItemses = getAll(urls);
        if (resultItemses != null && resultItemses.size() > 0) {
            return resultItemses.get(0);
        }
        return null;
    }

    
    public Spider addRequest(Request... requests) {
        for (Request request : requests) {
        	initRequest(request);
            addRequest(request);
        }
        signalNewUrl();
        if(this.site != null) {
        	site.setStartUrls(Site.requestToUrls(Arrays.asList(requests)));
        }
        return this;
    }

    private void waitNewUrl() {
        newUrlLock.lock();
        try {
            if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
                return;
            }
            newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LogUtils.warn("waitNewUrl - interrupted, error {"+e.getMessage()+"}");
        } finally {
            newUrlLock.unlock();
        }
    }

    private void signalNewUrl() {
        try {
            newUrlLock.lock();
            newUrlCondition.signalAll();
        } finally {
            newUrlLock.unlock();
        }
    }

    public void start() {
        runAsync();
    }

    public void stop() {
        if (stat.compareAndSet(STAT_RUNNING, STAT_STOPPED)) {
            LogUtils.info("Spider " + taskId() + " stop success!");
        } else {
        	LogUtils.info("Spider " + taskId() + " stop fail!");
        }
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: threadNum
     * @Description: 设置爬虫的线程数
     * @param @param threadNum
     * @param @return
     * @return Spider
     * @throws
     */
    public Spider threadNum(int threadNum) {
        checkIfRunning();
        this.threadNum = threadNum;
        if (threadNum <= 0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        return this;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: thread
     * @Description: 设置爬虫的线程池
     * @param @param executorService
     * @param @param threadNum
     * @param @return
     * @return Spider
     * @throws
     */
    public Spider thread(ExecutorService executorService, int threadNum) {
        checkIfRunning();
        this.threadNum = threadNum;
        if (threadNum <= 0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        return this;
    }

    public boolean isExitWhenComplete() {
        return exitWhenComplete;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setExitWhenComplete
     * @Description: 当所有URL全部爬取完毕,是否自动停止爬虫
     *               false表示不会自动停止爬虫,需要手动调用stop函数来停止爬虫
     * @param @param exitWhenComplete
     * @param @return
     * @return Spider
     * @throws
     */
    public Spider setExitWhenComplete(boolean exitWhenComplete) {
        this.exitWhenComplete = exitWhenComplete;
        return this;
    }

    public boolean isSpawnUrl() {
        return spawnUrl;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getPageCount
     * @Description: 返回爬虫已下载的网页数量
     * @param @return
     * @return long
     * @throws
     */
    public long getPageCount() {
        return pageCount.get();
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getStatus
     * @Description: 返回爬虫的运行状态
     * @param @return
     * @return Status
     * @throws
     */
    public Status getStatus() {
        return Status.fromValue(stat.get());
    }


    public enum Status {
        Init(0), Running(1), Stopped(2);

        private Status(int value) {
            this.value = value;
        }

        private int value;

        int getValue() {
            return value;
        }

        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            //默认值
            return Init;
        }
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getThreadAlive
     * @Description: 返回还处于活跃状态的爬虫线程数量
     * @param @return
     * @return int
     * @throws
     */
    public int getThreadAlive() {
        if (threadPool == null) {
            return 0;
        }
        return threadPool.getThreadAlive();
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setSpawnUrl
     * @Description: 是否自动发现新的URL,否则只抓取起始URL
     * @param @param spawnUrl
     * @param @return
     * @return Spider
     * @throws
     */
    public Spider setSpawnUrl(boolean spawnUrl) {
        this.spawnUrl = spawnUrl;
        return this;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: taskId
     * @Description: 爬虫的唯一标识
     * @param @return
     * @return String
     * @throws
     */
    @Override
    public String taskId() {
        if (uuid != null) {
            return uuid;
        }
        if (site != null) {
            return site.getDomain();
        }
        uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public Spider setExecutorService(ExecutorService executorService) {
        checkIfRunning();
        this.executorService = executorService;
        return this;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public List<SpiderListener> getSpiderListeners() {
        return spiderListeners;
    }

    public Spider setSpiderListeners(List<SpiderListener> spiderListeners) {
        this.spiderListeners = spiderListeners;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public Map<String, String> getHeaders() {
		return headers;
	}

	public Spider setHeaders(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public Spider setParams(Map<String, String> params) {
		this.params = params;
		return this;
	}
	
	public String getHeaderByKey(String key) {
		if(this.headers == null || this.headers.isEmpty()) {
			return null;
		}
		return this.headers.get(key);
	}
	
	public Spider addHeader(String key,String value) {
		if(this.headers == null) {
			this.headers = new HashMap<String, String>();
		}
		this.headers.put(key, value);
		return this;
	}
	
	public String getParamByKey(String key) {
		if(this.params == null || this.params.isEmpty()) {
			return null;
		}
		return this.params.get(key);
	}
	
	public Spider addParam(String key,String value) {
		if(this.params == null) {
			this.params = new HashMap<String, String>();
		}
		this.params.put(key, value);
		return this;
	}

	/**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: setEmptySleepTime
     * @Description: 当URL队列中没有URL可以取时，睡眠等待多长时间，单位：毫秒
     * @param @param emptySleepTime
     * @return void
     * @throws
     */
    public void setEmptySleepTime(int emptySleepTime) {
        this.emptySleepTime = emptySleepTime;
    }
    
    public int getInvokeTime() {
		return invokeTime;
	}

	public Spider setInvokeTime(int invokeTime) {
		this.invokeTime = invokeTime;
		return this;
	}
	
	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	public Spider setCountDownLatch(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
		return this;
	}

	public Login getLogin() {
		return login;
	}

	public Spider setLogin(Login login) {
		this.login = login;
		return this;
	}

	public Feedback getFeedback() {
		return feedback;
	}

	public Spider setFeedback(Feedback feedback) {
		this.feedback = feedback;
		return this;
	}

	private void initRequest(Request request) {
    	if(this.headers != null && !this.headers.isEmpty()) {
    		request.setHeaders(headers);
        }
    	if(this.params != null && !this.params.isEmpty()) {
    		request.setParams(params);
        }
    }
    
    public List<Request> convertToRequests(Collection<String> urls) {
        List<Request> requestList = new ArrayList<Request>(urls.size());
        for (String url : urls) {
            requestList.add(new Request(url));
        }
        return requestList;
    }
}
