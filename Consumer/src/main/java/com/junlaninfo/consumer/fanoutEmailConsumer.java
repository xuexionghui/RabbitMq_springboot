package com.junlaninfo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *  作者：xuexionghui
        邮箱：413669152@qq.com
        时间：2019年3月22日
        类作用：邮件队列消费者
 */

@Component 	
@RabbitListener(queues = "email_queue")
public class fanoutEmailConsumer {
	 //问题：如果消费端 程序业务逻辑出现异常，消息会消费成功吗？
	/*
	 * rabbitmq在默认情况下，如果消费者程序出现异常的情况下，会自动实现补偿机制
	 * 补偿机制 ： 由队列服务器发送补偿请求
	 * 原理：使用了@RabbitListener注解，其底层实现aop进行拦截，如果没有出现异常，那么自动提交事务；
	 *      如果出现了异常@RabbitListener会使用aop通知拦截异常信息，自动实现补偿机制，
	 *      该消息会缓存到rabbitmq队列服务器
	 *      进行存放，一直重试到不出现异常为止
	 */
	@RabbitHandler
	public void emailReceived(String msg) throws Exception {
		System.out.println(msg);
		//模拟异常
		//int i= 1/0;
	}

}
