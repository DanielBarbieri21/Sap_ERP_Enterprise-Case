package br.com.sap.erp.core.messaging;

import br.com.sap.erp.core.audit.AuditLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(AuditLog log) {
        rabbitTemplate.convertAndSend(RabbitConfig.AUDIT_QUEUE, log);
    }
}
