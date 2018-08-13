package com.sunsulei.spider.jd_spider.util;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDSpider {

    private static final String search = "https://search.jd.com/Search";


    public static void main(String[] args) {
//        search();

        getInfoByHtml("");

    }


    public static String search(String searchKey) {
        HttpRequest get = HttpUtil.createGet(search);

        get.form("keyword", searchKey)
                .form("enc", "utf-8")
                .form("pvid", "d3e0df70175441b3ac9125d7375521d3");

        get.header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("cache-control", "no-cache")
                .header("referer", "https://search.jd.com/Search?keyword=%E5%B0%8F%E7%B1%B3&enc=utf-8&pvid=ac4b26097009439585ca5f95f5732505")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36")
        ;


        String cookie = "__jda=122270672.1534077855183206362195.1534077855.1534077855.1534077855.1; __jdc=122270672; __jdv=122270672|direct|-|none|-|1534077855184; __jdu=1534077855183206362195; PCSYCityID=2; shshshfp=5dbb717855281dea2cf20d4aab41ab07; shshshfpa=64e7da59-02b1-0a7d-bd3f-842b140b25d3-1534077856; shshshfpb=18ae7a83c54bc4689b0c02d293615697132b2575cfa34e5f95b702b9e3; user-key=43e20beb-804b-4bdd-add6-a7ae877723a3; cn=0; xtest=6456.cf6b6759; ipLoc-djd=1-72-2799-0; __jdb=122270672.2.1534077855183206362195|1.1534077855; shshshsID=066270b4479a62013b3b1eecd6663c07_2_1534078427340; qrsc=1; rkv=V0300; 3AB9D23F7A4B3C9B=O5IOJBGCD4TXH7L5D22OLCH5LPXE3QDH5CQSUVDB3VSRMRVGANKDKOD5ZZYYGYOJSAIA2MPWFCGQHLWT5T2NNTST2A";
        get.cookie(cookie);

        HttpResponse execute = get.execute();

        int status = execute.getStatus();

        Console.log("查询接口,关键字:{},相应状态码{}.", searchKey, status);

        String body = execute.body();

        Console.log("查询接口,响应内容{}," , body);
        return body;
    }


    public static List<Map<String, Object>> getInfoByHtml(String htmlText) {

//        htmlText = TestRedisUtil.getJedis().get("xiaomipage1");
//        htmlText = TestRedisUtil.getJedis().get("string_applepage1");

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        Document doc = Jsoup.parse(htmlText);

        Elements skus = doc.getElementById("J_goodsList").getElementsByTag("li").nextAll(".gl-item");
        for (Element sku : skus) {
            map = new HashMap<>();
            String skuId = sku.attr("data-sku");
            String price = sku.getElementsByClass("J_" + skuId).first().getElementsByTag("i").text();
            Element tag_a = sku.getElementsByClass("p-name").first().getElementsByTag("a").first();
            Element em = tag_a.getElementsByTag("em").first();
            String title = tag_a.attr("title");

            if (em != null) {
                title = em.text() + title;
            }

            String url = "https://item.jd.com/" + skuId + ".html";

            Console.log("商品id:{}\t名称:{}\t价格:{}\t地址:{}", skuId, title, price, url);
            map.put("skuId", skuId);
            map.put("title", title);
            map.put("price", price);
            map.put("url", url);
            list.add(map);
        }

        Console.log();

        return list;
    }


}
