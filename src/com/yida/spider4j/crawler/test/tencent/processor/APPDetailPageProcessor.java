package com.yida.spider4j.crawler.test.tencent.processor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleDetailPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.Json;
import com.yida.spider4j.crawler.utils.common.FastJSonUtils;

/**
 * @ClassName: APPDetailPageProcessor
 * @Description: 腾讯应用宝APP详情页处理器
 * @author Lanxiaowei(736031305@qq.com)
 * @date 2015年10月26日 下午4:42:43
 *
 */
public class APPDetailPageProcessor extends SimpleDetailPageProcessor {
	public APPDetailPageProcessor(PageProcessorParam pageProcessorParam) {
		super(pageProcessorParam);
		
	}
	
	
	@Override
	public void process(Page page) {
		super.process(page);
		Json json = page.getJson();
		List<String> list = json.jsonPath("$.obj").all();
		//若返回的json中obj数组为空,则表明没有我们关心的数据,直接跳过不做任何处理
		if(null == list || list.size() <= 0) {
			return;
		}
		
		//遍历json obj数组
		for(String appJson : list) {
			//System.out.println("appJson:" + appJson);
			List<Map<String,Object>> mapList = FastJSonUtils.toListMap(appJson);
			for(Map<String,Object> map : mapList) {
				String jsonStr = FastJSonUtils.toJSon(map, null, null, false, false, true);
				json = new Json(jsonStr);
				LinkedHashMap<String, Object> dataMap = new LinkedHashMap<String, Object>();
				//APP ID
				dataMap.put("appId", json.jsonPath("$.appId").get());
				
				//APP名称
				dataMap.put("appName", json.jsonPath("$.appName").get());
				
				//APP当前最新版本号
				dataMap.put("versionName", json.jsonPath("$.versionName").get());
				
				//APP图标文件URL
				dataMap.put("iconUrl", json.jsonPath("$.iconUrl").get());
				
				//APP APK文件下载URL
				dataMap.put("apkUrl", json.jsonPath("$.apkUrl").get());
				
				//APP下载总次数
				dataMap.put("appDownCount", json.jsonPath("$.appDownCount").get());
				
				//APP分类名称
				dataMap.put("categoryName", json.jsonPath("$.categoryName").get());
				
				//APP开发作者
				dataMap.put("authorName", json.jsonPath("$.authorName").get());
				
				//APP新功能介绍
				dataMap.put("newFeature", json.jsonPath("$.newFeature").get());
				
				//APP发布时间,单位:毫秒
				dataMap.put("apkPublishTime", json.jsonPath("$.apkPublishTime").get());
				
				//APP平均得分
				dataMap.put("averageRating", json.jsonPath("$.averageRating").get());
				
				//APP宣传语
				dataMap.put("editorIntro", json.jsonPath("$.editorIntro").get());
				
				page.putMap(dataMap);
			}
			
		}
	}
}
