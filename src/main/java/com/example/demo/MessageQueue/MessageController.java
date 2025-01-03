package com.example.demo.MessageQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageProducer messageProducer;

    @Autowired
    public MessageController(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody String content) {
        Message message = new Message();
        message.setId(String.valueOf(System.currentTimeMillis()));
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now().toString());

        messageProducer.sendMessage(message);
        return "Message sent: " + message;
    }
}
