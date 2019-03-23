package com.junlaninfo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *  作者：xuexionghui
        邮箱：413669152@qq.com
        时间：2019年3月22日
        类作用：  消息队列消费者
 */
@Component
@RabbitListener(queues="msg_queue")
public class fanoutMsgConsumer {
	
	@RabbitHandler          //这个地方可以不和 生产者那里写的 msg 一样，也可以接受到消息
	public void msgRevieve(String msgs) {
	System.out.println(msgs);	
	}

}
