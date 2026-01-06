package com.xiaolong.xiaolong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableDiscoveryClient
public class XiaolongTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(XiaolongTaskApplication.class, args);
	}

}
