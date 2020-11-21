package com.service;

import com.model.Employee;

public interface JmsProducerService {
    void sendMessage(String message);
}
