package com.junlaninfo.config.fanout;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;



/**
 *  作者：xuexionghui
        邮箱：413669152@qq.com
        时间：2019年3月22日
        类作用：  配置类   配置发布订阅模式的配置
 */
@Component   
public class FanoutConfig {
	
	//邮件队列的名称
	private  String   email_queueName="email_queue";
	//消息队列的名称
	private  String   msg_queueName="msg_queue";
	//交换机的名称
	private  String   exchange_name="exchange_xuexionghui";
	
	
	// 1.定义队列
	    //定义邮件队列
	@Bean   //注入到spring容器中
	public  Queue  email_queue() {
		return  new Queue(email_queueName);
	}
	    //定义消息队列
	@Bean   //注入到spring容器中
	public  Queue  msg_queue() {
		return new Queue(msg_queueName);
	}
	
	// 2.定义交换机   使用fanout交换机 也就是使用  发布订阅模式
	@Bean
	public  FanoutExchange  fanout_exchange() {
		return new FanoutExchange(exchange_name);
	}
	
	// 3.队列与交换机绑定
	@Bean      //邮件队列和交换机绑定
	Binding bindingExchangeEamil(Queue  email_queue, FanoutExchange fanout_exchange) {
		return BindingBuilder.bind(email_queue).to(fanout_exchange);
	}
    
	@Bean      //消息队列和交换机绑定
	Binding bindingExchangeMsg(Queue  msg_queue, FanoutExchange fanout_exchange) {
		return BindingBuilder.bind(msg_queue).to(fanout_exchange);
	}
}
