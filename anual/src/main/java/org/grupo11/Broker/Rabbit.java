package org.grupo11.Broker;

import org.grupo11.Logger;
import org.grupo11.Config.Env;

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

    public void connect() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Env.getRabbitHost());
        factory.setUsername(Env.getRabbitUser());
        factory.setPassword(Env.getRabbitPassword());
        factory.setPort(Integer.parseInt(Env.getRabbitPort()));

        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare("temp_update", true, false, false, null);
            channel.queueDeclare("movement_update", true, false, false, null);
            setup_consumers(channel);
            Logger.info("Connection to rabbit successful");
        } catch (Throwable e) {
            Logger.error("Could not connect to rabbit", e);
            throw new Exception(e);
        }
    }

    public void setup_consumers(Channel channel) {
        try {
            channel.basicConsume("temp_update", true, (tag, message) -> Controller.handleTempUpdate(tag, message),
                    consumerTag -> {
                    });
            channel.basicConsume("movement_update", true,
                    (tag, message) -> Controller.handleMovementDetected(tag, message),
                    consumerTag -> {
                    });
        } catch (Exception e) {
            Logger.error("could not consume queues", e);
        }
    }

    public void send(String queue, String exchange, String message) {
        try {
            channel.basicPublish(exchange, queue, null, message.getBytes());
        } catch (Exception e) {
            Logger.error("Error while sending rabbit msg", e);
        }
    }
}
