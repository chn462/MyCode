package com.xiaolong.xiaolong.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Price implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Integer id;
    //币种 （暂支持：bitcoin、ethereum、solana、xrp、dogecoin、aave、chainlink）
    private String currency;
    //价格
    private BigDecimal priceUsd;
    //增长率
    private BigDecimal changePercent24Hr;
    //查询时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date queryTime;
    //查询时间戳
    private String queryTimestamp;
}
