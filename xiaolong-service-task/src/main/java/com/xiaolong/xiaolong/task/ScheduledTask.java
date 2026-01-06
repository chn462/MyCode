package com.xiaolong.xiaolong.task;

import com.xiaolong.xiaolong.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/*
 * 定时任务
 */
@Slf4j
@EnableAsync
@Component
public class ScheduledTask {

    @Autowired
    HttpClientUtils httpClientUtils;

    /*
     *  cron表达式 5分钟进行一次
     */
    @Async
    @Scheduled(cron = "0 */5 * * * ? ")
    @Scheduled(initialDelay = 5000)
    public void processCorn() throws ExecutionException, InterruptedException {
        log.info("定时任务开始：5分钟一次，调用httpClientUtils开始...");
        httpClientUtils.currencyClient();
    }
}
