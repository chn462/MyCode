package com.xiaolong.xiaolong.mapper;

import com.xiaolong.xiaolong.entity.Price;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PriceMapper {
//    @Select("SELECT * FROM currency_price WHERE id = #{id}")
    Price selectById(Integer id);

    Price selectForNow(String currency);

    List<Price> selectForDay(String currency);

    List<Price> selectForWeek(String currency);

    List<Price> selectForMonth(String currency);

    List<Price> selectFor3Month(String currency);

    int insert(Price price);

    int insertAll(List<Price> priceList);
}
