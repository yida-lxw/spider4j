package com.yida.spider4j.crawler.test.lianjia.processor;

import com.yida.spider4j.crawler.core.Page;
import com.yida.spider4j.crawler.processor.SimpleDetailPageProcessor;
import com.yida.spider4j.crawler.processor.param.PageProcessorParam;
import com.yida.spider4j.crawler.selector.ExpressionType;
import com.yida.spider4j.crawler.utils.common.StringUtils;

import java.util.List;

/**
 * @author Lanxiaowei(736031305@qq.com)
 * @ClassName: LianjiaDetailPageProcessor
 * @Description: 链家租房详情页处理器
 * @date 2017年12月22日 下午21:13:46
 */
public class LianJiaDetailPageProcessor extends SimpleDetailPageProcessor {
    public LianJiaDetailPageProcessor(PageProcessorParam pageProcessorParam) {
        super(pageProcessorParam);
    }

    @Override
    public void process(Page page) {
        super.process(page);
        if (null == page || null == page.getHtml()) {
            return;
        }
        if (StringUtils.isNotEmpty(page.getHtml(ExpressionType.JSOUP).jsoup("p[class=errorMessageInfo]").get())) {
            return;
        }
        //链家编号
        String lianjiaNO = page.getHtml(ExpressionType.JSOUP).jsoup("div[class=houseRecord] > span[class=houseNum]", "text").get();
        if (StringUtils.isNotEmpty(lianjiaNO)) {
            lianjiaNO = lianjiaNO.replace("链家编号：", "").replace("房源编号：", "").replaceAll("\\s+", "");
        }
        page.putField("lianjiaNO", lianjiaNO);

        //主标题
        String mainTitle = page.getHtml(ExpressionType.JSOUP).jsoup("div[class=content] > div[class=title]> h1[class=main]", "text").get();
        if (StringUtils.isNotEmpty(mainTitle)) {
            mainTitle = mainTitle.trim();
        } else {
            System.out.println("这个页面有问题：" + page.getRequest().getUrl());
            try {
                System.out.println(page.getHtml().getDocument());
                throw new Exception("哎呀出错啦！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        page.putField("mainTitle", mainTitle);

        //副标题
        String subTitle = page.getHtml(ExpressionType.JSOUP).jsoup("div[class=title] > div[class=sub]", "text").get();
        if (StringUtils.isNotEmpty(subTitle)) {
            subTitle = subTitle.replace("这个经纪人很懒，没写核心卖点", "").replaceAll("\\s+", "");
        }
        page.putField("subTitle", subTitle);

        //租金
        //北京
        //int rent = Integer.valueOf(page.getHtml(ExpressionType.JSOUP).jsoup("div[class^=price] > span[class=total]", "text").get().replaceAll("\\s+", ""));

        //苏州
        int rent = Integer.valueOf(page.getHtml(ExpressionType.JSOUP).jsoup("div[class^=price] > div[class=mainInfo bold]", "text").get()
                .replaceAll("元/月", "").replaceAll("\\s+", ""));
        page.putField("rent", rent);

        //小区
        //北京
        //String community = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='zf-room']/p/descendant::i[contains(text(),'小区')]/parent::p/a[1]", "text").get();
        //苏州
        String community = page.getHtml(ExpressionType.XPATH).xpath("//td[@class='title' and contains(text(),'小区')]/following-sibling::td[1]/p[@class='addrEllipsis']/a", "text").get();
        if (StringUtils.isNotEmpty(community)) {
            community = community.replace("（", "").replace("）", "");
        }
        page.putField("community", community);

        //面积
        double square = 0.00d;
        String squareStr = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='zf-room']/p[@class='lf']/descendant::i[contains(text(),'面积')]/parent::p[@class='lf']", "text").get();
        if (StringUtils.isNotEmpty(squareStr)) {
            if (squareStr.indexOf("(") == -1) {
                squareStr = squareStr.replace("面积：", "").replace("平米", "").trim();
            } else {
                squareStr = squareStr.substring(0, squareStr.indexOf("(")).replace("面积：", "").replace("平米", "").trim();
            }
            square = Double.valueOf(squareStr);
        }
        page.putField("square", square);

        //户型
        String houseType = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='zf-room']/p[@class='lf']/descendant::i[contains(text(),'房屋户型')]/parent::p[@class='lf']", "text").get();
        if (StringUtils.isNotEmpty(houseType)) {
            houseType = houseType.replace("房屋户型：", "").replaceAll("\\s+", " ");
        }
        page.putField("houseType", houseType);

        //楼层
        //北京
        //String floor = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='zf-room']/p[@class='lf']/descendant::i[contains(text(),'楼层')]/parent::p[@class='lf']", "text").get()
        //.replace("楼层：", "").replaceAll("\\s+", "");
        //苏州
        String floor = page.getHtml(ExpressionType.XPATH).xpath("//td[@class='title' and contains(text(),'楼层')]/following-sibling::td[1]", "text").get()
                .replace("楼层：", "").replaceAll("\\s+", "");
        page.putField("floor", floor);

        //房屋朝向
        //北京
        //String houseToward = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='zf-room']/p[@class='lf']/descendant::i[contains(text(),'房屋朝向')]/parent::p[@class='lf']", "text").get()
        //.replace("房屋朝向：", "");
        //苏州
        String houseToward = page.getHtml(ExpressionType.XPATH).xpath("//td[@class='title' and contains(text(),'朝向')]/following-sibling::td[1]", "text").get();
        if (StringUtils.isNotEmpty(houseToward)) {
            houseToward = houseToward.replace("朝向：", "").replace("暂无数据", "").replaceAll("\\s+", "");
        }
        page.putField("houseToward", houseToward);

        //位置
        //北京
        //String location = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='zf-room']/p/descendant::i[contains(text(),'位置')]/parent::p", "text").get()
        //.replace("位置：", "").replaceAll("\\s+", " ");
        //苏州
        String location = page.getHtml(ExpressionType.XPATH).xpath("//td[@class='title' and contains(text(),'地址')]/following-sibling::td[1]/p[@class='addrEllipsis']", "text").get();
        if (StringUtils.isNotEmpty(location)) {
            location = location.replace("地址：", "").replaceAll("\\s+", "").replace("（", "").replace("）", "");
        }
        page.putField("location", location);

        //地铁
        String subway = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='zf-room']/p/descendant::i[contains(text(),'地铁')]/parent::p", "text").get();
        if (StringUtils.isNotEmpty(subway)) {
            subway = subway.replace("地铁：", "").replace("暂无数据", "");
        }
        page.putField("subway", subway);

        //租赁方式
        String rentType = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='base']/div[@class='content']/ul/li/descendant::span[contains(text(),'租赁方式') and @class='label']/parent::li", "text").get();
        if (StringUtils.isNotEmpty(rentType)) {
            rentType = rentType.replace("租赁方式：", "").replace("暂无数据", "").replaceAll("\\s+", " ");
        }
        page.putField("rentType", rentType);

        //付款方式
        String payType = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='base']/div[@class='content']/ul/li/descendant::span[contains(text(),'付款方式') and @class='label']/parent::li", "text").get();
        if (StringUtils.isNotEmpty(payType)) {
            payType = payType.replace("付款方式：", "").replace("暂无数据", "").replaceAll("\\s+", " ");
        }
        page.putField("payType", payType);

        //房屋现状
        String houseState = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='base']/div[@class='content']/ul/li/descendant::span[contains(text(),'房屋现状') and @class='label']/parent::li", "text").get();
        if (StringUtils.isNotEmpty(houseState)) {
            houseState = houseState.replace("房屋现状：", "").replace("暂无数据", "").replaceAll("\\s+", " ");
        }
        page.putField("houseState", houseState);

        //供暖方式
        String heatingWay = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='base']/div[@class='content']/ul/li/descendant::span[contains(text(),'供暖方式') and @class='label']/parent::li", "text").get();
        if (StringUtils.isNotEmpty(heatingWay)) {
            heatingWay = heatingWay.replace("供暖方式：", "").replace("暂无数据", "").replaceAll("\\s+", " ");
        }
        page.putField("heatingWay", heatingWay);

        //房源亮点
        String houseFeature = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='featureContent']/ul/li/descendant::span[contains(text(),'房源亮点')]/parent::li/span[@class='text']", "text").get();
        page.putField("houseFeature", houseFeature);

        //户型介绍
        String houseIntroduced = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='featureContent']/ul/li/descendant::span[contains(text(),'户型介绍')]/parent::li/span[@class='text']", "text").get();
        page.putField("houseIntroduced", houseIntroduced);

        //装修描述
        String decorateDescription = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='featureContent']/ul/li/descendant::span[contains(text(),'装修描述')]/parent::li/span[@class='text']", "text").get();
        page.putField("decorateDescription", decorateDescription);

        //周边配套
        String houseAround = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='featureContent']/ul/li/descendant::span[contains(text(),'周边配套')]/parent::li/span[@class='text']", "text").get();
        page.putField("houseAround", houseAround);

        //交通出行
        String traffic = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='featureContent']/ul/li/descendant::span[contains(text(),'交通出行')]/parent::li/span[@class='text']", "text").get();
        page.putField("traffic", traffic);

        //经纪人
        String brokerName = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='brokerName']/a[1]", "text").get();
        page.putField("brokerName", brokerName);

        //经纪人联系电话
        String brokerPhone = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='brokerInfoText']/div[@class='phone']", "text").get();
        if (StringUtils.isNotEmpty(brokerPhone)) {
            brokerPhone = brokerPhone.replaceAll("\\s+", "");
        }
        page.putField("brokerPhone", brokerPhone);

        //经纪人评分
        String brokerScoreStr = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='brokerInfoText']/div[@class='evaluate']/span[@class='rate']", "text").get();
        String appraiseCount = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='brokerInfoText']/div[@class='evaluate']/span[@class='rate']/a[1]", "text").get();
        if (StringUtils.isNotEmpty(brokerScoreStr)) {
            brokerScoreStr = brokerScoreStr.replace("评分:", "").replace("暂无评价", "").replaceAll("\\s+", "");
        }
        if (StringUtils.isNotEmpty(appraiseCount) && StringUtils.isNotEmpty(brokerScoreStr)) {
            brokerScoreStr = brokerScoreStr.replace(appraiseCount, "").replaceAll("/", "").trim();
        }
        double brokerScore = 0.00D;
        if (StringUtils.isNotEmpty(brokerScoreStr)) {
            brokerScore = Double.valueOf(brokerScoreStr);
        }
        page.putField("brokerScore", brokerScore);

        //房屋设备
        List<String> equipments = page.getHtml(ExpressionType.XPATH).xpath("//div[@class='introContent']/div[@class='feature']/div[@class='zf-tag']/ul[@class='se']/li", "text").all();
        String equipment = null;
        if (null != equipments && equipments.size() > 0) {
            equipment = "";
            for (String equip : equipments) {
                if (!equip.contains("业主可配")) {
                    equip = equip + "(无)";
                } else {
                    equip = equip.replace("业主可配", "有");
                }
                equipment = equipment + equip.replaceAll("\\s+", " ") + ",";
            }
            if (equipment.endsWith(",")) {
                equipment = equipment.substring(0, equipment.lastIndexOf(","));
            }
        }
        page.putField("equipment", equipment);

        //房源照片
        String houseImg = page.getHtml(ExpressionType.XPATH).xpath("//div[@id='topImg']/div[@class='imgContainer']/img/attribute::src").get();
        page.putField("houseImg", houseImg);

        System.out.println("/***************************************房源信息：[" + lianjiaNO + "] begin ***************************************/");
        System.out.println("链家编号:" + lianjiaNO);
        System.out.println("主标题:" + mainTitle);
        System.out.println("副标题:" + subTitle);
        System.out.println("租金/月:￥" + rent);
        System.out.println("小区:" + community);
        System.out.println("房屋面积:" + square);
        System.out.println("房屋户型:" + houseType);
        System.out.println("楼层:" + floor);
        System.out.println("房屋朝向:" + houseToward);
        System.out.println("位置:" + location);
        System.out.println("地铁:" + subway);
        System.out.println("房屋设备:" + equipment);
        System.out.println("房源照片:" + houseImg);
        System.out.println("租赁方式:" + rentType);
        System.out.println("付款方式:" + payType);
        System.out.println("房屋现状:" + houseState);
        System.out.println("供暖方式:" + heatingWay);
        System.out.println("房源亮点:" + houseFeature);
        System.out.println("户型介绍:" + houseIntroduced);
        System.out.println("装修描述:" + decorateDescription);
        System.out.println("周边配套:" + houseAround);
        System.out.println("交通出行:" + traffic);
        System.out.println("经纪人:" + brokerName);
        System.out.println("经纪人联系电话:" + brokerPhone);
        System.out.println("经纪人评分:" + brokerScore);
        System.out.println("/***************************************房源信息：[" + lianjiaNO + "] end ***************************************/\n\n");
    }
}
