package com.study.rabbit.workfair;

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
        /**
         * 每个消费者，发送确认消息之前，消息队列不发送下一个消息给消费者
         * 一次只处理一个消息
         *
         * 限制发送给同一个消费者 不得超过一条消息
         */
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        for (int i = 0; i < 100; i++) {
            String str = "Hello work queue " + i;
            channel.basicPublish("", QUEUE_NAME, null, str.getBytes());
            Thread.sleep(i * 10L);
        }
        channel.close();
        connection.close();


    }
}
