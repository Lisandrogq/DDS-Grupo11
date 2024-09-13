package org.grupo11;

import org.grupo11.Controller.Controller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Rabbit {
    private static Rabbit instance = null;
    private Channel channel;

    public static synchronized Rabbit getInstance() {
        if (instance == null)
            instance = new Rabbit();

        return instance;
    }

    public void connect() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("fridgebridge");
        factory.setPassword("fridgebridge");
        factory.setPort(5672);

        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare("System Alerts", false, false, false, null);
            channel.queueDeclare("Temp Updates", true, false, false, null);
            channel.queueDeclare("Opening Checks", true, false, false, null);

            setup_consumers(channel);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void setup_consumers(Channel channel) {
        try {
            channel.basicConsume("Temp Updates", true, (tag, message) -> Controller.handle_temp_update(tag, message),
                    consumerTag -> {
                    });

            channel.basicConsume("System Alerts", true, (tag, message) -> Controller.handle_alert(tag, message),
                    consumerTag -> {
                    });
            channel.basicConsume("Opening Checks", true,
                    (tag, message) -> Controller.handle_opening_request(tag, message),
                    consumerTag -> {
                    });

        } catch (Exception e) {
            System.err.println("could not connect consume queues");
        }
    }

    public void send(String queue, String exchange, String message) {
        try {
            channel.basicPublish(exchange, queue, null, message.getBytes());
        } catch (Exception e) {
            System.err.println("could not send message");
        }
    }
}
