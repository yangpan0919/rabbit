package com.study.rabbit.simple;

import com.rabbitmq.client.*;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeoutException;

public class Recv {
    private static final String QUEUE_NAME = "first_simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //定义队列的消费者
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String str = new String(body, "utf-8");
                System.out.println(str + "|||" + Instant.now().toEpochMilli());
            }
        };
        //监听队列
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);

    }
}
