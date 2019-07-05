package com.study.rabbit.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String EXCHANGE_NAME = "first_exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String msg = "hello direct";
        String routingKey = "info";
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
        channel.close();
        connection.close();
    }

}
