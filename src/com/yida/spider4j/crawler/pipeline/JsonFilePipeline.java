package com.yida.spider4j.crawler.pipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.yida.spider4j.crawler.config.DefaultConfigurable;
import com.yida.spider4j.crawler.core.PageResultItem;
import com.yida.spider4j.crawler.task.Task;
import com.yida.spider4j.crawler.utils.common.FastJSonUtils;
import com.yida.spider4j.crawler.utils.common.StringUtils;
import com.yida.spider4j.crawler.utils.log.LogUtils;

/**
 * @ClassName: JsonFilePipeline
 * @Description: 存储PageResultItem数据至Json格式文件里
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月15日 上午10:12:49
 *
 */
public class JsonFilePipeline extends DefaultConfigurable implements Pipeline {
	/**数据存储目录*/
	private String savePath;
	/**文件写入编码*/
	private String writeCharset;
	
	private static final String PATH_SEPERATOR = System.getProperty("file.separator");
	
    public JsonFilePipeline() {
        this.savePath = this.config.getCrawlStorageFolder();
        this.writeCharset = this.config.getPageDefaultEncoding();
    }

    public JsonFilePipeline(String savePath) {
    	this.savePath = savePath;
    	this.writeCharset = this.config.getPageDefaultEncoding();
    }
    
    public JsonFilePipeline(String savePath,String writeCharset) {
    	this.savePath = savePath;
    	this.writeCharset = writeCharset;
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
            	new OutputStreamWriter(new FileOutputStream(new File(path + StringUtils.md5Hex(pageResultItem.getRequest().getUrl()) + ".json")),writeCharset));
            String json = FastJSonUtils.toJSon(pageResultItem.getAll(), 
            	null, null, true, false, true);
            if(StringUtils.isNotEmpty(json)) {
            	printWriter.println(json);
            }
            printWriter.close();
        } catch (IOException e) {
            LogUtils.warn("write json file error:\n" + e.getMessage());
        }
    }
}
