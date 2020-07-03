package com.study.rabbit.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String EXCHANGE_NAME = "test";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
        String msg = "这解释隔离。、！@#￥%……fslkdjf";
        String routingKey = "127.0.0.2";
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
        channel.close();
        connection.close();
    }

}
