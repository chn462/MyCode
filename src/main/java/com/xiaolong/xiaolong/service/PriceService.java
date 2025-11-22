package com.xiaolong.xiaolong.service;

import com.xiaolong.xiaolong.entity.Price;

import java.util.List;

public interface PriceService {
    Price selectById(Integer id);

    //查当前价格，数据量1条
    Price selectForNow(String currency);

    //查当天价格，间隔15分钟，数据量96条
    List<Price> selectForDay(String currency);

    //查一周价格，间隔2小时，数据量84条
    List<Price> selectForWeek(String currency);

    //查一周价格，间隔2小时，数据量120条
    List<Price> selectForMonth(String currency);

    //查三月价格，间隔1天，数据量90条
    List<Price> selectFor3Month(String currency);

    int insert(Price price);

    int insertAll();
}
