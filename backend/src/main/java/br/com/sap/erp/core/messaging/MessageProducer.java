package br.com.sap.erp.core.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendFinancialMessage(Map<String, Object> message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FINANCIAL_QUEUE, message);
    }

    public void sendSalesMessage(Map<String, Object> message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.SALES_QUEUE, message);
    }

    public void sendPurchaseMessage(Map<String, Object> message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.PURCHASE_QUEUE, message);
    }

    public void sendNotification(Map<String, Object> notification) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_QUEUE, notification);
    }
}
