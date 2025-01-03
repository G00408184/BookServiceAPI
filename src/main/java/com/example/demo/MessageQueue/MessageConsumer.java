package com.example.demo.MessageQueue;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @RabbitListener(queues = "productQueue")
    public void receiveMessage(Message message) {
        System.out.println("Message received from queue: " + message);
    }
}

