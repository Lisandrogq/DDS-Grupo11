package org.grupo11.Controller;

import java.io.IOException;
import com.rabbitmq.client.Delivery;

public class Controller {

    public Controller() {

    }

    public static void handleAlert(String consumerTag, Delivery message) throws IOException {
        System.out.println("pija de goma");

    }

    public static void fridges(String consumerTag, Delivery message) throws IOException {
        System.out.println("pija de goma");
    }
}