package com.xiaolong.xiaolong.utils;

import com.alibaba.fastjson2.JSON;
import com.xiaolong.xiaolong.entity.Price;
import com.xiaolong.xiaolong.mapper.PriceMapper;
import com.xiaolong.xiaolong.service.RedisManualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * httpClient工具类
 */
@Slf4j
@Component
@Transactional
public class HttpClientUtils {

    //currency币种 （暂支持：bitcoin、ethereum、solana、xrp、dogecoin、aave、chainlink）
    private static final String[] currencys = {"bitcoin", "ethereum", "solana", "xrp", "dogecoin", "aave", "chainlink"};

    @Autowired
    private PriceMapper priceMapper;

    @Autowired
    private RedisManualService redisManualService;

    public void currencyClient() throws ExecutionException, InterruptedException {
        log.info("httpClient发送开始.... ");

        // 创建线程池，例如，使用7个线程的线程池
        ExecutorService executor = Executors.newFixedThreadPool(7);
        List<Future<HttpResponse<String>>> futures = new ArrayList<>();

        for (String currency : currencys) {
            // 创建HttpClient实例
            HttpClient client = HttpClient.newHttpClient();
            // 构建HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://rest.coincap.io/v3/assets/" + currency + "?apiKey=6e8ef80d8d10645aa54a7db0fb478e9eb5b7d822bdd9a4ec71ad958ecab68cf3"))
                    .build();
            Future<HttpResponse<String>> future = executor.submit(() -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join());
            futures.add(future);
        }

        // 等待所有请求完成并处理结果
        for (Future<HttpResponse<String>> future : futures) {
            // 获取结果，这里会阻塞直到响应返回或抛出异常
            HttpResponse<String> response = future.get();
//            log.info("httpClient响应体---- {}", response.body());
            // 检查状态码
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                // 接下来可以解析jsonResponse...
                Map mapType = JSON.parseObject(jsonResponse, Map.class);
                Price price = new Price();
                for (Object obj : mapType.keySet()) {
//                    log.info("key为： {}  值为： {}", obj, mapType.get(obj));
                    if ("timestamp".equals(obj) && !"null".equals(String.valueOf(mapType.get(obj)))) {
                        price.setQueryTime(new Date((Long) mapType.get(obj)));
                        price.setQueryTimestamp(String.valueOf(mapType.get(obj)));
                    } else if ("data".equals(obj) && !"null".equals(String.valueOf(mapType.get(obj)))) {
                        Map data = JSON.parseObject(String.valueOf(mapType.get(obj)), Map.class);
                        for (Object dataObj : data.keySet()) {
                            if ("id".equals(dataObj)) {
                                price.setCurrency(String.valueOf(data.get(dataObj)));
                            } else if ("priceUsd".equals(dataObj)) {
                                price.setPriceUsd(BigDecimal.valueOf(Double.parseDouble("" + data.get(dataObj))));
                            } else if ("changePercent24Hr".equals(dataObj)) {
                                price.setChangePercent24Hr(BigDecimal.valueOf(Double.parseDouble("" + data.get(dataObj))));
                            }
                        }
                    }
                }
                log.info("打印price---- {}", price);
                int insert = priceMapper.insert(price);
                redisManualService.setObject("now::" + price.getCurrency(), price, 5, TimeUnit.MINUTES);
                log.info("打印插入成功标志---- {}", insert);
            } else {
                log.info("Request failed. Status code: {}", response.statusCode());
            }
        }
        executor.shutdown(); // 关闭线程池
        log.info("httpClient线程池关闭，发送结束.... ");
    }
}
