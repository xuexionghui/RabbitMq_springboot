package com.junlaninfo.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//Fanout 配置
@Component
public class FanoutConfig {

	// 邮件队列的名字
	private String FANOUT_EMAIL_QUEUE = "fanout_email_queue";

	// 消息队列的名字
	private String FANOUT_SMS_QUEUE = "fanout_sms_queue";
	// fanout  交换机名
	private String EXCHANGE_NAME = "fanoutExchange";

	// 1.注入队列   邮件
	@Bean
	public Queue fanOutEamilQueue() {
		return new Queue(FANOUT_EMAIL_QUEUE);
	}

	// 2.注入队列  消息
	@Bean
	public Queue fanOutSmsQueue() {
		return new Queue(FANOUT_SMS_QUEUE);
	}

	// 2.注入交换机
	@Bean
	FanoutExchange fanoutExchange() {
		return new FanoutExchange(EXCHANGE_NAME);
	}

	// 3.交换机绑定邮件队列
	@Bean
	Binding bindingExchangeEamil(Queue fanOutEamilQueue, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(fanOutEamilQueue).to(fanoutExchange);
	}

	// 4.交换机绑定消息队列
	@Bean
	Binding bindingExchangeSms(Queue fanOutSmsQueue, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(fanOutSmsQueue).to(fanoutExchange);
	}
}
