package com.example.demo.MessageQueue;

import lombok.Data;

@Data
public class Message {
    private String id;
    private String content;
    private String timestamp;
}
