package com.study.rabbit.tx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String QUEUE_NAME = "first_queue_tx";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        try {
            String msg = "hello tx";
            int i = 1/0;
            channel.txSelect();
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            channel.txCommit();
        } catch (IOException e) {
            channel.txRollback(); //回滚
        } finally {
        }
        channel.close();
        connection.close();
    }
}
