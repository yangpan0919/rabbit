package com.study.rabbit.simple;

import com.rabbitmq.client.*;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeoutException;

public class Recv {
    private static final String QUEUE_NAME = "first_simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //定义队列的消费者
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String str = new String(body, "utf-8");
                System.out.println(str + "|||" + Instant.now().toEpochMilli());
//               手动应答是除了确认应答，也可以拒绝应答。
//               requeue=true,表示将消息重新放入到队列中，false：表示直接从队列中删除，此时和basicAck(long deliveryTag, false)的效果一样
//                void basicReject(long deliveryTag, boolean requeue);
//                channel.basicAck(envelope.getDeliveryTag(), false);    手动应答
            }
        };
        //监听队列
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);//true  自动应答， false 手动（必须执行该方法：basicAck(envelope.getDeliveryTag(), false);   ）

    }
}
