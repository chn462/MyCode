package com.xiaolong.xiaolong.mapper;

import com.xiaolong.xiaolong.entity.Price;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PriceMapper {
//    @Select("SELECT * FROM currency_price WHERE id = #{id}")
    Price selectById(Integer id);

    Price selectForNow(String currency);

    List<Price> selectForDay(String currency);

    List<BigDecimal> selectForWeek(String currency);

    List<BigDecimal> selectForMonth(String currency);

    List<BigDecimal> selectFor3Month(String currency);

    int insert(Price price);

    int insertAll(List<Price> priceList);
}
