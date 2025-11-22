package com.xiaolong.xiaolong.controller;

import com.xiaolong.xiaolong.entity.Price;
import com.xiaolong.xiaolong.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/price")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/{id}")
    public Price getPriceService(@PathVariable Integer id) {
        return priceService.selectById(id);
    }

    @GetMapping("/selectForNow/{currency}")
    public Price selectForNow(@PathVariable String currency) {
        return priceService.selectForNow(currency);
    }

    @GetMapping("/selectForDay/{currency}")
    public List<Price> selectForDay(@PathVariable String currency) {
        return priceService.selectForDay(currency);
    }

    @GetMapping("/selectForWeek/{currency}")
    public List<Price> selectForWeek(@PathVariable String currency) {
        return priceService.selectForWeek(currency);
    }

    @GetMapping("/selectForMonth/{currency}")
    public List<Price> selectForMonth(@PathVariable String currency) {
        return priceService.selectForMonth(currency);
    }

    @GetMapping("/selectFor3Month/{currency}")
    public List<Price> selectFor3Month(@PathVariable String currency) {
        return priceService.selectFor3Month(currency);
    }

//    @GetMapping("/insertAll")
    public Integer insertAll() {
        return priceService.insertAll();
    }
}
