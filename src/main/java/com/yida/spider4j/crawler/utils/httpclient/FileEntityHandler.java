package com.yida.spider4j.crawler.utils.httpclient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;

import com.yida.spider4j.crawler.utils.common.GerneralUtils;
import com.yida.spider4j.crawler.utils.io.FileUtils;

/**
 * Response响应对象to File处理器
 * @author Lanxiaowei
 */
public class FileEntityHandler implements EntityHandler<File>{
	/**目标文件保存路径*/
	private String filePath;
	/**保存文件名称*/
	private String fileName;
	/**默认下载目录*/
	private static final String SAVE_PATH = "C:/temp/DOWNLOAD";

    public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

    public FileEntityHandler(){}
    
    public FileEntityHandler(String filePath,String fileName) {
    	this.filePath = filePath;
    	this.fileName = fileName;
    }

    public File handleEntity(HttpEntity entity) throws IOException {
		if(GerneralUtils.isEmptyString(filePath)) {
			filePath = SAVE_PATH;
		}
    	File folder = new File(filePath);
    	if(!folder.exists()) {
    		folder.mkdirs();
    	}
    	if(GerneralUtils.isEmptyString(fileName)) {
    		fileName = "New-File.temp";
    	}
    	File file = new File(FileUtils.getFullFilePath(fileName, filePath));
    	OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
    	entity.writeTo(os);
    	os.flush();
    	os.close();
    	return file;
    }
}
