package com.study.rabbit.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class Send3 {

    private static final String QUEUE_NAME = "first_queue_confirm3";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        //创建队列申明
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //将channel设置为confirm模式
        channel.confirmSelect();

        //未确认的消息标志
        SortedSet<Long> set = Collections.synchronizedSortedSet(new TreeSet<>());
        //添加通道监听
        channel.addConfirmListener(new ConfirmListener() {
            //没有问题的
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("handleAck   多个的");
                    set.headSet(deliveryTag + 1).clear();
                } else {
                    System.out.println("handleAck   单个的");
                    set.remove(deliveryTag);
                }

            }

            //有问题的
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("nack   多个的");
                    set.headSet(deliveryTag + 1).clear();
                } else {
                    System.out.println("nack   单个的");
                    set.remove(deliveryTag);
                }
            }
        });


        String msg = "hello confirm 3";

        while (true) {
            long seq = channel.getNextPublishSeqNo();
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            set.add(seq);
        }

//        channel.close();
//        connection.close();

    }
}
