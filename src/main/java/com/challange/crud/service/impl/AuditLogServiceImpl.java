package com.challange.crud.service.impl;

import com.challange.crud.model.AuditLog;
import com.challange.crud.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditLogServiceImpl implements com.challange.crud.service.AuditLogService {
    @Autowired
    private AuditLogRepository auditLogRepository;

    public void logAudit(String message) {

        AuditLog auditLog = AuditLog.builder()
                .message(message)
                .timestamp(new Date())
                .build();

        auditLogRepository.save(auditLog);
    }
}
