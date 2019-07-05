package com.study.rabbit.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send1 {

    private static final String QUEUE_NAME = "first_queue_confirm1";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        //创建队列申明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //将channel设置为confirm模式
        channel.confirmSelect();
        String msg = "hello confirm 1";
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());

        if (channel.waitForConfirms()) {
            System.out.println("OK");
        } else {
            System.out.println("false");
        }
        channel.close();
        connection.close();

    }
}
