package com.junlaninfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  作者：xuexionghui
        邮箱：413669152@qq.com
        时间：2019年3月22日
        类作用： 采用RabbitMq的发布订阅模式 发送信息
 */
@SpringBootApplication
public class ProApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProApplication.class, args);

	}

}
