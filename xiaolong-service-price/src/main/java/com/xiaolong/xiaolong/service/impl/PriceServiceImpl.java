package com.xiaolong.xiaolong.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.xiaolong.xiaolong.entity.Price;
import com.xiaolong.xiaolong.mapper.PriceMapper;
import com.xiaolong.xiaolong.service.PriceService;
import com.xiaolong.xiaolong.service.RedisManualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class PriceServiceImpl implements PriceService {

    @Autowired
    private PriceMapper priceMapper;

    @Autowired
    private RedisManualService redisManualService;

    @Override
    public Price selectById(Integer id) {
        return priceMapper.selectById(id);
    }

    //查当前价格，数据量1条
    @Override
    public Price selectForNow(String currency) {
        Price priceInRedis = (Price) redisManualService.getObject("now::" + currency);
        if (priceInRedis != null) {
            return priceInRedis;
        } else {
            Price price = priceMapper.selectForNow(currency);
            return price;
        }
    }

    //查当天价格，间隔15分钟，数据量96条
    @Override
    public List<BigDecimal> selectForDay(String currency) {
        List<BigDecimal> priceListInRedis = (List<BigDecimal>) redisManualService.getObject("day::" + currency);
        if (priceListInRedis != null && priceListInRedis.size() >= 96) {
            return priceListInRedis;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long fifteenMinutes = 15 * 60 * 1000;
        // 方法返回集
        List<BigDecimal> pricesResult = new ArrayList<>();
        // 数据库获取结果集
        List<Price> prices = priceMapper.selectForDay(currency);
        String format = sdf.format(prices.get(0).getQueryTime());
        String queryDay0 = format.substring(0, 10) + " 00:00:00";
        // prices中第一天 00：00：00 的时间戳
        long timestamp = 0;
        try {
            timestamp = sdf.parse(queryDay0).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < prices.size(); i++) {
            Price price = prices.get(i);
            Date queryTime = price.getQueryTime();
            long time = queryTime.getTime();
            long count = (time - timestamp) / fifteenMinutes;
            if (time >= timestamp && count >= 0) {
                pricesResult.add(price.getPriceUsd());
                timestamp += fifteenMinutes * (count + 1);
            }
        }
        if (pricesResult.size() > 96) {
            List<BigDecimal> result = new ArrayList<>(pricesResult.subList(pricesResult.size() - 96, pricesResult.size()));
            redisManualService.setObject("day::" + currency, result, 15, TimeUnit.MINUTES);
            return result;
        } else {
            redisManualService.setObject("day::" + currency, pricesResult, 15, TimeUnit.MINUTES);
            return pricesResult;
        }
    }

    //查一周价格，间隔2小时，数据量84条
    @Override
    public List<BigDecimal> selectForWeek(String currency) {
        List<BigDecimal> priceListInRedis = (List<BigDecimal>) redisManualService.getObject("week::" + currency);
        if (priceListInRedis != null && priceListInRedis.size() >= 84) {
            return priceListInRedis;
        }
        List<BigDecimal> prices = priceMapper.selectForWeek(currency);
        if (prices == null && prices.size() == 0) {
            return List.of();
        } else if (prices.size() < 84) {
            return prices;
        } else if (prices.size() >= 84) {
            List<BigDecimal> result = new ArrayList<>(prices.subList(prices.size() - 84, prices.size()));
            redisManualService.setObject("week::" + currency, result, 2, TimeUnit.HOURS);
            return result;
        } else {
            return prices;
        }
    }

    //查一月价格，间隔6小时，数据量120条
    @Override
    public List<BigDecimal> selectForMonth(String currency) {
        List<BigDecimal> priceListInRedis = (List<BigDecimal>) redisManualService.getObject("month::" + currency);
        if (priceListInRedis != null && priceListInRedis.size() >= 120) {
            return priceListInRedis;
        }
        List<BigDecimal> prices = priceMapper.selectForMonth(currency);
        if (prices == null && prices.size() == 0) {
            return List.of();
        } else if (prices.size() < 120) {
            return prices;
        } else if (prices.size() >= 120) {
            List<BigDecimal> result = new ArrayList<>(prices.subList(prices.size() - 120, prices.size()));
            redisManualService.setObject("month::" + currency, result, 6, TimeUnit.HOURS);
            return result;
        } else {
            return prices;
        }
    }

    //查三月价格，间隔1天，数据量90条
    @Override
    public List<BigDecimal> selectFor3Month(String currency) {
        List<BigDecimal> priceListInRedis = (List<BigDecimal>) redisManualService.getObject("3month::" + currency);
        if (priceListInRedis != null && priceListInRedis.size() > 0) {
            return priceListInRedis;
        }
        List<BigDecimal> prices = priceMapper.selectFor3Month(currency);
        if (prices == null && prices.size() == 0) {
            return List.of();
        } else if (prices.size() < 90) {
            return prices;
        } else if (prices.size() >= 90) {
            List<BigDecimal> result = new ArrayList<>(prices.subList(prices.size() - 90, prices.size()));
            redisManualService.setObject("3month::" + currency, result, 1, TimeUnit.DAYS);
            return result;
        } else {
            return prices;
        }
    }

    @Override
    public int insert(Price price) {
        return priceMapper.insert(price);
    }

    //用于数据库添加数据
    @Override
    public int insertAll() {
        String priceJson = "";
        JSONArray jsonArray = JSONArray.parseArray(priceJson);
        List<Price> priceList = new ArrayList<Price>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Price price = new Price();
            price.setCurrency("chainlink");
            price.setPriceUsd(jsonObject.getBigDecimal("priceUsd"));
            price.setQueryTime(jsonObject.getDate("date"));
            priceList.add(price);
        }
        int insertAll = priceMapper.insertAll(priceList);
        return insertAll;
    }

}
