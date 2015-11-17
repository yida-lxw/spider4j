package com.yida.spider4j.crawler.proxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

import org.apache.http.HttpHost;

import com.yida.spider4j.crawler.config.DefaultConfigurable;
import com.yida.spider4j.crawler.utils.io.FileUtils;
import com.yida.spider4j.crawler.utils.log.LogUtils;
import com.yida.spider4j.crawler.utils.proxy.ProxyUtils;

/**
 * @ClassName: ProxyPool
 * @Description: Http代理池
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月16日 下午1:33:39
 *
 */
@SuppressWarnings("unchecked")
public class ProxyPool extends DefaultConfigurable {
	/**Http代理队列*/
    private BlockingQueue<Proxy> proxyQueue = new DelayQueue<Proxy>();
    private Map<String, Proxy> allProxy = new ConcurrentHashMap<String, Proxy>();

    /**重用Http代理的时间间隔,单位:毫秒*/
    private int reuseInterval = 1500;// ms
    /**Http代理复活的时间间隔,单位:毫秒*/
    private int reviveTime = 2 * 60 * 60 * 1000;// ms
    /**保存Http代理的时间间隔,单位:毫秒*/
    private int saveProxyInterval = 10 * 60 * 1000;// ms
    
    /**http代理存储目录*/
    private String proxyFilePath;

    private boolean isEnable = false;
    /**初始化时是否验证Http代理*/
    private boolean validateWhenInit = false;

    private Timer timer = new Timer(true);
    private TimerTask saveProxyTask = new TimerTask() {

        @Override
        public void run() {
            saveProxyList();
            LogUtils.info(allProxyStatus());
        }
    };

    public ProxyPool() {
        this(null, true);
    }

    public ProxyPool(List<String[]> httpProxyList) {
        this(httpProxyList, true);
    }

    public ProxyPool(List<String[]> httpProxyList, boolean isUseLastProxy) {
        if (httpProxyList != null) {
            addProxy(httpProxyList.toArray(new String[httpProxyList.size()][]));
        }
        if (isUseLastProxy) {
            if (!new File(proxyFilePath).exists()) {
                setFilePath();
            }
            readProxyList();
            timer.schedule(saveProxyTask, 0, saveProxyInterval);
        }
    }

    private void setFilePath() {
        this.proxyFilePath = checkFilePath();
    }
    
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: checkFilePath
     * @Description: 检查代理文件及父目录是否存在,若不存在则新建
     * @param @return
     * @return String
     * @throws
     */
    private String checkFilePath() {
    	String path = this.config.getLastuseProxyDir();
    	int index = path.lastIndexOf(FileUtils.PATH_SEPERATOR);
    	if(index > 0) {
    		String parentPath = path.substring(0,index);
            File parent = new File(parentPath);
            if(!parent.exists()) {
            	parent.mkdirs();
            }
    	}
        
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
            	LogUtils.error("proxy file create error:\n" + e.getMessage());
            }
        }
        return path;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: saveProxyList
     * @Description: 保存Http代理至文件
     * @param 
     * @return void
     * @throws
     */
    private void saveProxyList() {
        if (allProxy.size() == 0) {
            return;
        }
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(new File(proxyFilePath)));
            os.writeObject(prepareForSaving());
            os.close();
            LogUtils.info("save proxy");
        } catch (FileNotFoundException e) {
        	LogUtils.error("proxy file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Proxy> prepareForSaving() {
        Map<String, Proxy> tmp = new HashMap<String, Proxy>();
        for (Entry<String, Proxy> e : allProxy.entrySet()) {
            Proxy p = e.getValue();
            p.setFailedNum(0);
            tmp.put(e.getKey(), p);
        }
        return tmp;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: readProxyList
     * @Description: 从文件中读取Http代理列表
     * @param 
     * @return void
     * @throws
     */
	private void readProxyList() {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File(proxyFilePath)));
            addProxy((Map<String, Proxy>) is.readObject());
            is.close();
        } catch (FileNotFoundException e) {
        	LogUtils.info("last use proxy file not found");
        } catch (IOException e) {
        	LogUtils.info("Read last use proxy file occur exception.");
        } catch (ClassNotFoundException e) {
        	LogUtils.info("Read object from last use proxy file,but not found.");
        }
    }

    private void addProxy(Map<String, Proxy> httpProxyMap) {
        isEnable = true;
        for (Entry<String, Proxy> entry : httpProxyMap.entrySet()) {
            try {
                if (allProxy.containsKey(entry.getKey())) {
                    continue;
                }
                //若不需要验证Http代理或Http代理验证通过了,则添加到Http代理队列里
                if (!validateWhenInit || ProxyUtils.validateProxy(entry.getValue().getHttpHost())) {
                    entry.getValue().setFailedNum(0);
                    entry.getValue().setReuseTimeInterval(reuseInterval);
                    proxyQueue.add(entry.getValue());
                    allProxy.put(entry.getKey(), entry.getValue());
                }
            } catch (NumberFormatException e) {
                LogUtils.error("HttpHost init error:\n" + e.getMessage());
            }
        }
        LogUtils.info("proxy pool size>>>>" + allProxy.size());
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: addProxy
     * @Description: 添加Http代理
     * @param @param httpProxyList
     * @return void
     * @throws
     */
    public void addProxy(String[]... httpProxyList) {
        isEnable = true;
        for (String[] s : httpProxyList) {
            try {
                if (allProxy.containsKey(s[0])) {
                    continue;
                }
                HttpHost item = new HttpHost(InetAddress.getByName(s[0]), Integer.valueOf(s[1]));
                if (!validateWhenInit || ProxyUtils.validateProxy(item)) {
                    Proxy p = new Proxy(item, reuseInterval);
                    proxyQueue.add(p);
                    allProxy.put(s[0], p);
                }
            } catch (NumberFormatException e) {
            	LogUtils.error("HttpHost init error:\n" + e.getMessage());
            } catch (UnknownHostException e) {
            	LogUtils.error("HttpHost init error:\n" + e.getMessage());
            }
        }
        LogUtils.info("proxy pool size>>>>" + allProxy.size());
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getProxy
     * @Description: 从Http代理队列中获取一个代理
     * @param @return
     * @return HttpHost
     * @throws
     */
    public HttpHost getProxy() {
        Proxy proxy = null;
        try {
            Long time = System.currentTimeMillis();
            proxy = proxyQueue.take();
            double costTime = (System.currentTimeMillis() - time) / 1000.0;
            if (costTime > reuseInterval) {
                LogUtils.info("get proxy time >>>> " + costTime);
            }
            Proxy p = allProxy.get(proxy.getHttpHost().getAddress().getHostAddress());
            p.setLastBorrowTime(System.currentTimeMillis());
            p.borrowNumIncrement(1);
        } catch (InterruptedException e) {
            LogUtils.error("get proxy error:\n" + e.getMessage());
        }
        if (proxy == null) {
            throw new NoSuchElementException();
        }
        return proxy.getHttpHost();
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: returnProxy
     * @Description: 归还一个Http代理至Http代理队列中
     * @param @param host
     * @param @param statusCode
     * @return void
     * @throws
     */
    public void returnProxy(HttpHost host, int statusCode) {
        Proxy p = allProxy.get(host.getAddress().getHostAddress());
        if (p == null) {
            return;
        }
        switch (statusCode) {
            case Proxy.SUCCESS:
                p.setReuseTimeInterval(reuseInterval);
                p.setFailedNum(0);
                p.setFailedErrorType(new ArrayList<Integer>());
                p.recordResponse();
                p.successNumIncrement(1);
                break;
            case Proxy.ERROR_403:
                // banned,try longer interval
                p.fail(Proxy.ERROR_403);
                p.setReuseTimeInterval(reuseInterval * p.getFailedNum());
                LogUtils.info(host + " >>>> reuseTimeInterval is >>>> " + p.getReuseTimeInterval() / 1000.0);
                break;
            case Proxy.ERROR_BANNED:
                p.fail(Proxy.ERROR_BANNED);
                p.setReuseTimeInterval(10 * 60 * 1000 * p.getFailedNum());
                LogUtils.warn("this proxy is banned >>>> " + p.getHttpHost());
                LogUtils.info(host + " >>>> reuseTimeInterval is >>>> " + p.getReuseTimeInterval() / 1000.0);
                break;
            case Proxy.ERROR_404:
                //p.fail(Proxy.ERROR_404);
                //p.setReuseTimeInterval(reuseInterval * p.getFailedNum());
                break;
            default:
                p.fail(statusCode);
                break;
        }
        if (p.getFailedNum() > 20) {
            p.setReuseTimeInterval(reviveTime);
            LogUtils.error("remove proxy >>>> " + host + ">>>>" + p.getFailedType() + " >>>> remain proxy >>>> " + proxyQueue.size());
            return;
        }
        if (p.getFailedNum() > 0 && p.getFailedNum() % 5 == 0) {
            if (!ProxyUtils.validateProxy(host)) {
                p.setReuseTimeInterval(reviveTime);
                LogUtils.error("remove proxy >>>> " + host + ">>>>" + p.getFailedType() + " >>>> remain proxy >>>> " + proxyQueue.size());
                return;
            }
        }
        try {
            proxyQueue.put(p);
        } catch (InterruptedException e) {
            LogUtils.warn("proxyQueue return proxy error:\n" + e.getMessage());
        }
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: allProxyStatus
     * @Description: 返回所有http代理的状态
     * @param @return
     * @return String
     * @throws
     */
    public String allProxyStatus() {
        String re = "all proxy info >>>> \n";
        for (Entry<String, Proxy> entry : allProxy.entrySet()) {
            re += entry.getValue().toString() + "\n";
        }
        return re;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getIdleNum
     * @Description: 返回Http代理队列中空闲的Http代理个数
     * @param @return
     * @return int
     * @throws
     */
    public int getIdleNum() {
        return proxyQueue.size();
    }

    public int getReuseInterval() {
        return reuseInterval;
    }

    public void setReuseInterval(int reuseInterval) {
        this.reuseInterval = reuseInterval;
    }

    public void enable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public int getReviveTime() {
        return reviveTime;
    }

    public void setReviveTime(int reviveTime) {
        this.reviveTime = reviveTime;
    }

    public boolean isValidateWhenInit() {
        return validateWhenInit;
    }

    public void validateWhenInit(boolean validateWhenInit) {
        this.validateWhenInit = validateWhenInit;
    }

    public int getSaveProxyInterval() {
        return saveProxyInterval;
    }

    public void setSaveProxyInterval(int saveProxyInterval) {
        this.saveProxyInterval = saveProxyInterval;
    }

    public String getProxyFilePath() {
        return proxyFilePath;
    }

    public void setProxyFilePath(String proxyFilePath) {
        this.proxyFilePath = proxyFilePath;
    }
}
