package com.study.rabbit.routing;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.study.rabbit.util.ConnectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String EXCHANGE_NAME = "test";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
        MessageData messageData = new MessageData();

        messageData.setType(0);
        messageData.setDeviceCode("plasma_02");
        messageData.setKey("uploadRecipe");
        List<String> data = new ArrayList<>();
        data.add("et");
        messageData.setData(data);
        Gson gson = new Gson();
        String s = gson.toJson(messageData);

        String msg = "这解释隔离。、！@#￥%……fslkdjf";
        String routingKey = "127.0.0.1";
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, s.getBytes());
        channel.close();
        connection.close();
    }

}
