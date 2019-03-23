package com.junlaninfo.rabbitmq;

import java.util.UUID;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class fanoutProducer {
	@Autowired
	private AmqpTemplate amqpTemplate;

	public void send(String queueName) {
		//jsonString:{"email":"413669152@qq.com","timestamp":1553326311339}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("email", "413669152@qq.com");
		jsonObject.put("timestamp", System.currentTimeMillis());
		//将json格式的数据转化为String类型
		String jsonString = jsonObject.toJSONString();
		System.out.println("jsonString:" + jsonString);
		
		//发送json格式的数据  // 生产者发送消息的时候需要设置消息id
		Message message = MessageBuilder.withBody(jsonString.getBytes())
				.setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8").setMessageId(UUID.randomUUID() + "").build();

		amqpTemplate.convertAndSend(queueName, message);
	}
}
