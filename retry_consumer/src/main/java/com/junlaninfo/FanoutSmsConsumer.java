package com.junlaninfo;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//�ʼ�����
@Component
public class FanoutSmsConsumer {
	@RabbitListener(queues = "fanout_sms_queue")
	public void process(String msg) {
		System.out.println("���������߻�ȡ��������Ϣmsg:" + msg);
	}
}
