package com.junlaninfo;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.junlaninfo.HttpClientUtils;

import com.rabbitmq.client.Channel;

//�ʼ�����
@Component
public class FanoutEamilConsumer {
      
	  @Resource
	    private RedisTemplate<String, Object> redisTemplate;
	  private String msgId="messageId";
	// rabbitmq 默认情况下 如果消费者程序出现异常（手动抛出异常或者自动抛出异常都可以出发重试机制）的情况下，会自动实现补偿机制
		// 补偿（重试机制） 队列服务器 发送补偿请求
		// 如果消费端 程序业务逻辑出现异常消息会消费成功吗？
		
		// @RabbitListener 底层 使用Aop进行拦截，如果程序没有抛出异常，自动提交事务
		// 如果Aop使用异常通知拦截 获取异常信息的话，自动实现补偿机制 ，该消息会缓存到rabbitmq服务器端进行存放，一直重试到不抛异常为准。

		// 修改重试机制策略 一般默认情况下 间隔5秒重试一次
	
	     //第一种 ： 演示重试机制
	/*    @RabbitListener(queues="fanout_email_queue")   //接收从邮件队列传来的消息
	    public void  process(Message  message) throws Exception   {
	    	String msg = new  String(message.getBody(),"UTF-8");    //获得消息的内容
	    	
	    	JSONObject jsonObject = JSONObject.parseObject(msg);
	    	String  email = jsonObject.getString("email");
	    	
	    	String url="http://127.0.0.1:8083/sendEmail?email="+email;
	    	System.out.println("邮件消费者开始调用第三方邮件服务器,Url:" + url);
	    	//模拟调用第三方的接口
	    	JSONObject result=HttpClientUtils.httpGet(url);
	    	//手动抛出异常 触发重试的机制
	    	if(result==null) {
	    		throw new Exception("调用失败");
	    	}
	    	//没有问题，程序执行完成
	    	System.out.println("邮件消费者结束调用第三方邮件服务器成功,result:" + result + "程序执行结束");
	    }
	*/
	
	
        /*   第2中， 解决mq重试机制可能引发的幂等性问题
         * MQ重试机制需要注意的问题
         *    在集群的时候，可能造成消费被重复消费 ，要考虑幂等性的问题
	       MQ消费者幂等性问题如何解决：使用全局ID
	             思路：
	           a/生产者在发送消息的时候，在消息那里加一个id（必须是唯一的，可以是订单号或者随机数）
	             Message message = MessageBuilder.withBody(jsonString.getBytes())
				.setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8")
		          // 生产者发送消息的时候需要设置消息id
		        .setMessageId(UUID.randomUUID() + "").build();
		        b/消息消费成功以后，将此id值存入到redis中，而在消费消息前，先判断如果已经有消费消息，就直接返回
         */
		

	@RabbitListener(queues = "fanout_email_queue")
	public void process(Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
		String messageId = message.getMessageProperties().getMessageId();
		String msg = new String(message.getBody(), "UTF-8");
		System.out.println("邮件消费者获取生产者消息msg:" + msg + ",消息id:" + messageId);
		// 解决重试机制可能造成的幂等性问题，造成消息的重复读取
		 String string = redisTemplate.execute(new RedisCallback<String>() {
	            @Override
	            public String doInRedis(RedisConnection connection) throws DataAccessException {
	                byte[] codeBytes = connection
	                        .get(redisTemplate.getStringSerializer().serialize("messageId:" + msgId));
	                return redisTemplate.getStringSerializer().deserialize(codeBytes);
	            }

	        });
		 if(string!=null&&string.equals(messageId)) {
			 return ;
		 }
		JSONObject jsonObject = JSONObject.parseObject(msg);
		String email = jsonObject.getString("email");
		String emailUrl = "http://127.0.0.1:8083/sendEmail?email=" + email;
		System.out.println("邮件消费者开始调用第三方邮件服务器,emailUrl:" + emailUrl);
		JSONObject result = HttpClientUtils.httpGet(emailUrl);
		// 如果调用第三方邮件接口无法访问，如何实现自动重试.
		
		if (result == null) {
			throw new Exception("调用第三方邮件服务器接口失败!");
		}
		System.out.println("邮件消费者结束调用第三方邮件服务器成功,result:" + result + "程序执行结束");
		//消息消费成功，将messageId存入redis中
		  redisTemplate.execute(new RedisCallback<String>() {
	        	@Override
	        	public String doInRedis(RedisConnection connection) throws DataAccessException {
	        		connection.setEx(redisTemplate.getStringSerializer().serialize("messageId:" + msgId),
	        				5000, redisTemplate.getStringSerializer().serialize(messageId+ ""));
	        		return null;
	        	}
	        });
		System.out.println("messageId是："+messageId);
		
		// 手动ack  
		/*Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
		// 手动签收
		channel.basicAck(deliveryTag, false);*/

	}
	// 默认是自动应答模式


}
