package com.challange.crud.service.impl;

import com.challange.crud.service.AuditLogService;
import com.challange.crud.service.KafkaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    @Autowired
    private AuditLogService auditLogService;

    @KafkaListener(topics = "learn", groupId = "audit-logger-group")
    public void consume(String message) {
        System.out.println("Received message: " + message);
        // Log the received message to the audit table
        auditLogService.logAudit(message);
    }
}
