package com.xiaolong.xiaolong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class XiaolongPriceApplication {

	public static void main(String[] args) {
		SpringApplication.run(XiaolongPriceApplication.class, args);
	}

}
