package com.yida.spider4j.crawler.controller;

import com.yida.spider4j.crawler.bean.HotNews;
import com.yida.spider4j.crawler.service.HotNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private HotNewsService hotNewsService;

    @GetMapping("/index")
    public String index(Model model) {
        List<HotNews> hotNewsList = hotNewsService.getTopNHotNews();
        List<HotNews> hotNewsYearList = hotNewsService.getTopNHotNewsOfYear();
        List<HotNews> hotNewsMonthList = hotNewsService.getTopNHotNewsOfMonth();
        List<HotNews> hotNewsDayList = hotNewsService.getTopNHotNewsOfDay();
        model.addAttribute("hotNewsList", hotNewsList);
        model.addAttribute("hotNewsYearList", hotNewsYearList);
        model.addAttribute("hotNewsMonthList", hotNewsMonthList);
        model.addAttribute("hotNewsDayList", hotNewsDayList);
        return "index";
    }
}
