package com.study.rabbit.componet;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MyBean {

//	@RabbitListener(queues = "someQueue")
	public void processMessage(String content) {
		// ...
	}

}