package com.junlaninfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junlaninfo.config.fanout.FanoutProducer;

/**
 *  作者：xuexionghui
        邮箱：413669152@qq.com
        时间：2019年3月22日
        类作用：
 */
@RestController
public class producerController {
	
	@Autowired
	private FanoutProducer fanoutProducer;
	
	@GetMapping(value="/send")
	public  String   send(String queueName) {
		fanoutProducer.send(queueName);
		return "success";
	}

}
