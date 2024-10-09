package com.challange.crud.service.impl;

import com.challange.crud.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "learn";

    @Override
    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
