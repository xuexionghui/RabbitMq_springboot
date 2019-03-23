package com.junlaninfo.config.fanout;

import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  作者：xuexionghui
        邮箱：413669152@qq.com
        时间：2019年3月22日
        类作用：
 */
@Component
public class FanoutProducer {
     
	@Autowired
	private AmqpTemplate   amqpTemplate;
	
	public void send( String queueName ) {
		String msg = "my_fanout_msg:" + new Date();
		System.out.println(msg + ":" + msg);
		amqpTemplate.convertAndSend(queueName, msg);
	}

}
