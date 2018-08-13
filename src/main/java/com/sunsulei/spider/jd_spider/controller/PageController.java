package com.sunsulei.spider.jd_spider.controller;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.sunsulei.spider.jd_spider.common.JsonResult;
import com.sunsulei.spider.jd_spider.common.RedisUtil;
import com.sunsulei.spider.jd_spider.util.JDSpider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class PageController {

    @RequestMapping(value = {"", "index"})
    public String index(ModelMap map) {

        String applepage1 = RedisUtil.get("applepage1", String.class);
        List<Map<String, Object>> apple = JDSpider.getInfoByHtml(applepage1);
        map.put("apple", apple);

        String xiaomipage1 = RedisUtil.get("xiaomipage1", String.class);
        List<Map<String, Object>> xiaomi = JDSpider.getInfoByHtml(xiaomipage1);
        map.put("xiaomi", xiaomi);
        return "index";
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult search(String searchKey) {
        Console.log(searchKey);
        String htmlInfo;
        if (StrUtil.isBlank(searchKey)) {
            searchKey = "apple";
        }
        htmlInfo = RedisUtil.get(searchKey, String.class);
        if (StrUtil.isBlank(htmlInfo)) {
            htmlInfo = JDSpider.search(searchKey);
            RedisUtil.set(searchKey, htmlInfo, -1L);
        }
        List<Map<String, Object>> infoByHtml = JDSpider.getInfoByHtml(htmlInfo);
        return JsonResult.resultSuccess(infoByHtml);
    }

    @RequestMapping(value = "getItem", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getItem(String url) {
        Console.log(url);

//        String content = HttpUtil.get(url);
        HttpRequest get = HttpUtil.createGet(url);

        HttpResponse res = get.execute();

        res.charset("gbk");

        String body = res.body();


        return JsonResult.resultSuccess(body);
    }

}
