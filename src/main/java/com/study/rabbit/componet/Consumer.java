package com.study.rabbit.componet;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    /**
     * 监听替补队列 来验证死信.
     *
     *
     *死信队列 听上去像 消息“死”了 其实也有点这个意思，死信队列 是 当消息在一个队列 因为下列原因：
     *
     * 1.消息被拒绝（basic.reject/ basic.nack）并且不再重新投递 requeue=false
     * 2.消息超期 (rabbitmq Time-To-Live -> messageProperties.setExpiration())
     * 3.队列超载
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = "REDIRECT_QUEUE")
    @RabbitHandler
    public void redirect(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        logger.info("dead message  10s 后 消费消息 :" + new String(message.getBody()));
    }

    /**
     * 如果没有接收该消息处理，那么十秒后将进去死信队列处理
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = "DL_QUEUE")
    public void receive(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        System.out.println("收到的消息：" + new String(message.getBody()));
    }

}
