package br.com.sap.erp.core.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FINANCIAL_QUEUE = "financial.queue";
    public static final String SALES_QUEUE = "sales.queue";
    public static final String PURCHASE_QUEUE = "purchase.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";

    @Bean
    public Queue financialQueue() {
        return new Queue(FINANCIAL_QUEUE, true);
    }

    @Bean
    public Queue salesQueue() {
        return new Queue(SALES_QUEUE, true);
    }

    @Bean
    public Queue purchaseQueue() {
        return new Queue(PURCHASE_QUEUE, true);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
