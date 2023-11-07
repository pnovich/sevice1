package com.example.service1;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketProducerSender implements TicketSender {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    ModelMapper modelMapper;

    private String senderType = "kafka sender";

    private volatile int kafkaCount;


    public void sendTicket(String topic, Ticket ticket) {
        kafkaTemplate.send(topic, modelMapper.map(ticket, TicketDTO.class));
        kafkaCount++;
    }

    @Override
    public String getSenderType() {
        return this.senderType;
    }

    @Override
    public int getCount() {
        return kafkaCount;
    }

}
