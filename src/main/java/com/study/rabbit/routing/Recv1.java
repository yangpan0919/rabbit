package com.study.rabbit.routing;

import com.rabbitmq.client.*;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv1 {
    private static final String QUEUE_NAME = "first_queue_direct_1";
    private static final String EXCHANGE_NAME = "first_exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.basicQos(1);

        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"error");
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
