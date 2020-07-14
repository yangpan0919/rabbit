package com.study.rabbit.simple;

import com.rabbitmq.client.*;
import com.study.rabbit.config.RabbitMQConstant;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Recv {
    private static final String QUEUE_NAME = "192.168.2.221";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        //定义队列的消费者
        /**
         * queue ：队列名称
         * durable ： 是否持久化。true持久化，队列会保存磁盘。服务器重启时可以保证不丢失相关信息。
         * exclusive ：设置是否排他。true排他的。如果一个队列声明为排他队列，该队列仅对首次声明它的连接可见，并在连接断开时自动删除。
         * 排它是基于连接可见的，同一个连接不同信道是可以访问同一连接创建的排它队列，“首次”是指如果一个连接已经声明了一个排他队列，其他连接是不允许建立同名的排他队列，即使这个队列是持久化的，一旦连接关闭或者客户端退出，该排它队列会被自动删除，这种队列适用于一个客户端同时发送与接口消息的场景。
         *
         * autoDelete :设置是否自动删除。true是自动删除。自动删除的前提是：致少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开 时，才会自动删除
         * 生产者创建这个队列，或者没有消费者客户端与这个队列连接时，都不会自动删除这个队列
         *
         * arguments ：设置队列的一些其它参数。如
         * x-message-ttl,x-expires等。
         */
        Map<String, Object> arguments = new HashMap<>();
        //消息超时时间30s
        arguments.put(RabbitMQConstant.TTL_TIME, 30000);
        channel.queueDeclare(QUEUE_NAME, false, false, true, arguments);
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
