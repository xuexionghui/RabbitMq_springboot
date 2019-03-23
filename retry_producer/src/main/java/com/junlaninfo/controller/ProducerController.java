package com.junlaninfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.junlaninfo.rabbitmq.fanoutProducer;

@RestController
public class ProducerController {
	@Autowired
	private fanoutProducer producer;

	@RequestMapping("/sendFanout")
	public String sendFanout(String queueName) {
		producer.send(queueName);
		return "success";
	}
}
