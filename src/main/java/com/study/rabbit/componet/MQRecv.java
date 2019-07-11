package com.study.rabbit.componet;

import com.rabbitmq.client.Channel;
import com.study.rabbit.config.RabbitMQConstant;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MQRecv {


    int tempCount = 0;

//    @RabbitListener(queues = "myqueue")
//    public void receive(String message) {
//        System.out.println(++tempCount);
//        System.out.println("错误的：" + message);
//    }

    @RabbitListener(
            bindings = @QueueBinding(
                    exchange = @Exchange(value = RabbitMQConstant.DEFAULT_EXCHANGE, type = ExchangeTypes.TOPIC,
                            durable = RabbitMQConstant.FALSE_CONSTANT, autoDelete = RabbitMQConstant.TRUE_CONSTANT),
                    value = @Queue(value = RabbitMQConstant.DEFAULT_QUEUE, durable = RabbitMQConstant.FALSE_CONSTANT,
                            autoDelete = RabbitMQConstant.TRUE_CONSTANT, arguments = {
                            @Argument(name = RabbitMQConstant.DEAD_LETTER_EXCHANGE, value = RabbitMQConstant.DEAD_EXCHANGE),
                            @Argument(name = RabbitMQConstant.DEAD_LETTER_KEY, value = RabbitMQConstant.DEAD_KEY)
                    }),
                    key = RabbitMQConstant.DEFAULT_KEY
            ))
    public void receive1(String message) {
        System.out.println(++tempCount);
        int i = 1 / 0;
        System.out.println("收到的 message 是：" + message);
    }

//    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = RabbitMQConstant.DEFAULT_QUEUE,durable = "false",autoDelete = "true",
//            arguments = {@Argument(name = "x-dead-letter-exchange", value = RabbitMQConstant.DEAD_EXCHANGE)}),
//            exchange = @Exchange(value = "exchange"))})
//    public void receive2(String message) {
//        System.out.println("不知道什么队列,不知道能不能实现" + message);
//    }


    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "test",
            arguments = {@Argument(name = "x-dead-letter-exchange", value = "some.exchange.name")}),
            exchange = @Exchange(value = "exchange"))})
    public void receive3(String message) {
        System.out.println(++tempCount);
        int i = 1 / 0;
        System.out.println("收到的 message 是：" + message);

    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "test",
            arguments = {@Argument(name = "x-dead-letter-exchange", value = "some.exchange.name")}),
            exchange = @Exchange(value = "exchange"))})
    public void receive4(String message) {
//        System.out.println(++tempCount);
//        int i = 1 / 0;
        System.out.println("收到的 message 是：" + message);

    }

    //    @RabbitListener(queues = "first_queue_direct1")
//    public void receive5(String message) {
//        System.out.println("收到的 message1 是：" + message);
//    }
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "first_queue_direct1"),
            exchange = @Exchange(value = "first_topic_exchange", type = "topic"), key = "goods.update")
    })
    public void receive5(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //手动应答
        System.out.println("收到的 message1 是：" + new String(message.getBody()));
    }


    @RabbitListener(queues = "first_queue_direct2")
    public void receive6(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //手动应答
        System.out.println("收到的 message2 是：" + new String(message.getBody()));

    }

}
