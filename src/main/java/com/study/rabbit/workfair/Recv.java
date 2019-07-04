package com.study.rabbit.workfair;

import com.rabbitmq.client.*;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {
    private static final String QUEUE_NAME = "first_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //定义队列的消费者
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicQos(1); //保证一次分发一个

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String str = new String(body, "utf-8");

                System.out.println("[1]   " + str);
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        //监听队列
        channel.basicConsume(QUEUE_NAME, false, defaultConsumer);//true  自动应答， false 手动（必须执行该方法：basicAck(envelope.getDeliveryTag(), false);   ）

    }
}
