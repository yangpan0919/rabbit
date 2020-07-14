package com.study.rabbit.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeoutException;

/**
 * 简单队列的不足
 * 耦合性高，生产者一一对应消费者（如果我想有多个消费者消费队列中的消息，不行）
 * 队列名称变更，同时变更
 */
public class Send {
    private static final String QUEUE_NAME = "192.168.2.221";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        //创建队列申明
//        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        String msg = "hello simple ";
        for (int i = 0; i < 100; i++) {
            String temp = msg + i;
            channel.basicPublish("", QUEUE_NAME, null, temp.getBytes());
            System.out.println(i + "|||" + Instant.now().toEpochMilli());
        }


        System.out.println("fasong jieshu ");
        channel.close();
        connection.close();

    }
}
