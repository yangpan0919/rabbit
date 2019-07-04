package com.study.rabbit.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 轮询分发，只能是你一个我一个，分配到的数量相等
 */
public class Send {
    private static final String QUEUE_NAME = "first_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        for (int i = 0; i < 100; i++) {
            String str = "Hello work queue " + i;
            channel.basicPublish("", QUEUE_NAME, null, str.getBytes());
            Thread.sleep(i * 20L);
        }
        channel.close();
        connection.close();


    }
}
