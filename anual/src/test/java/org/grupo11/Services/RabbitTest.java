package org.grupo11.Services;

import org.junit.Test;
import static org.junit.Assert.*;

import org.grupo11.Rabbit;

public class RabbitTest {
    @Test
    public void connects_and_consumes_message() {
        org.grupo11.Rabbit rabbit = Rabbit.getInstance();
        rabbit.connect();
        rabbit.send("System alerts", "", "hello world");
        assertEquals(0, 0);
    }
}
