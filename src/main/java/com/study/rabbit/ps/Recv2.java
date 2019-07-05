package com.study.rabbit.ps;

import com.rabbitmq.client.*;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv2 {
    private static final String QUEUE_NAME = "first_queue_fanout_sms";
    private static final String EXCHANGE_NAME = "first_exchange_fanout";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
                channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");
        channel.basicQos(1); //保证一次分发一个

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String str = new String(body, "utf-8");

                System.out.println("[2]   " + str);
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
