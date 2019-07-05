package com.study.rabbit.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send2 {

    private static final String QUEUE_NAME = "first_queue_confirm2";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        //创建队列申明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //将channel设置为confirm模式
        channel.confirmSelect();
        String msg = "hello confirm 2";
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            Thread.sleep(2000L);
        }
        /**
         * 批量Confirm模式：
         * 每发送一批消息之后，调用waitForConfirms()方法，
         * 等待服务端Confirm，这种批量确认的模式极大的提高了Confirm效率，
         * 但是如果一旦出现Confirm返回false或者超时的情况，
         * 客户端需要将这一批次的消息全部重发，这会带来明显的重复消息，
         * 如果这种情况频繁发生的话，效率也会不升反降；
         */
        if (channel.waitForConfirms()) {
            System.out.println("OK");
        } else {
            System.out.println("false");
        }
        channel.close();
        connection.close();

    }
}
