package com.study.rabbit.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String EXCHANGE_NAME = "first_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String msg = "商品 topic";
        String routingKey = "goods.11111";
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
        channel.close();
        connection.close();
    }

}
