package com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsProducerServiceImpl implements JmsProducerService {
    private static final Logger LOG = LoggerFactory.getLogger(JmsProducerService.class);

    private final JmsTemplate jmsTemplate;

    @Autowired
    public JmsProducerServiceImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void sendMessage(String message) {
        try {
            LOG.info("Attempting Send message to Topic: " + "employee.topic");
            jmsTemplate.convertAndSend("employee.topic", message);
        } catch (Exception e) {
            LOG.error("Recieved Exception during send Message: ", e);
        }
    }
}
