package com.yida.spider4j.crawler.pipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

import com.yida.spider4j.crawler.config.DefaultConfigurable;
import com.yida.spider4j.crawler.core.PageResultItem;
import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.log.LogUtils;

/**
 * @ClassName: FilePipeline
 * @Description: 存储PageResultItem数据至文件里
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 上午10:12:49
 *
 */
@SuppressWarnings("rawtypes")
public class FilePipeline extends DefaultConfigurable implements Pipeline {
	/**数据存储目录*/
	private String savePath;
	/**文件写入编码*/
	private String writeCharset;
	
	/**多值情况下,多值的拼接分隔符,默认为*/
	private String multiSeparator = "#$&#";
	
	private static final String PATH_SEPERATOR = System.getProperty("file.separator");
	
    public FilePipeline() {
        this.savePath = this.config.getCrawlStorageFolder();
        this.writeCharset = this.config.getPageDefaultEncoding();
    }

    public FilePipeline(String savePath) {
    	this.savePath = savePath;
    	this.writeCharset = this.config.getPageDefaultEncoding();
    }
    
    public FilePipeline(String savePath,String writeCharset) {
    	this.savePath = savePath;
    	this.writeCharset = writeCharset;
    }
    
    public FilePipeline(String savePath,String writeCharset,String multiSeparator) {
    	this.savePath = savePath;
    	this.writeCharset = writeCharset;
    	this.multiSeparator = multiSeparator;
    }

	@Override
    public void process(PageResultItem pageResultItem, Task task) {
		if(pageResultItem == null || pageResultItem.getDataMap() == null || 
				pageResultItem.getDataMap().isEmpty()) {
				return;
			}
        String path = this.savePath + task.taskId() + PATH_SEPERATOR;
        try {
            PrintWriter printWriter = new PrintWriter(
            	new OutputStreamWriter(new FileOutputStream(new File(path + StringUtils.md5Hex(pageResultItem.getRequest().getUrl()) + ".html")),writeCharset));
            printWriter.println("url:\t" + pageResultItem.getRequest().getUrl());
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, Object> entry : pageResultItem.getAll().entrySet()) {
                if (entry.getValue() instanceof Iterable) {
                    Iterable value = (Iterable) entry.getValue();
                    printWriter.print(entry.getKey() + "-->");
                    for (Object o : value) {
                    	builder.append(o.toString()).append(multiSeparator);
                    }
                    String okay = builder.toString().replaceAll("#\\$&#$", "");
                    printWriter.println(okay);
                } else {
                    printWriter.println(entry.getKey() + "-->" + entry.getValue());
                }
            }
            printWriter.close();
        } catch (IOException e) {
            LogUtils.warn("write file error:\n" + e.getMessage());
        }
    }
}
