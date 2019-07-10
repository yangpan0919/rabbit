package com.study.rabbit.controller;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@RequestMapping("/dead")
@Controller
public class SendController {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 测试死信队列. http://localhost:8082/dead/deadLetter?p=11234
	 */
	@RequestMapping("/deadLetter")
	@ResponseBody
	public String deadLetter(String p) {
		p = "first to dead";
		CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

		// 声明消息处理器 这个对消息进行处理 可以设置一些参数 对消息进行一些定制化处理 我们这里 来设置消息的编码 以及消息的过期时间
		// 因为在.net 以及其他版本过期时间不一致 这里的时间毫秒值 为字符串
		MessagePostProcessor messagePostProcessor = message -> {
			MessageProperties messageProperties = message.getMessageProperties();
			// 设置编码
			messageProperties.setContentEncoding("utf-8");
			// 设置过期时间10*1000毫秒
			messageProperties.setExpiration("10000");
			return message;
		};
		// 向DL_QUEUE 发送消息 10*1000毫秒后过期 形成死信,具体的时间可以根据自己的业务指定
		rabbitTemplate.convertAndSend("DL_EXCHANGE", "DL_KEY", p, messagePostProcessor, correlationData);
		return "sucess";
	}

}

