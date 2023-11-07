package com.example.service1;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @PostConstruct
    public void kafkaSenderSetup() {
        ticketProducerSender.sendTicket("all_tickets2", new Ticket(1, "test_ticket"));
    }

    @Autowired
    TicketProducerSender ticketProducerSender;

    @Autowired
    TicketRestSender ticketRestSender;

    private final static String targetUrl = "http://localhost:8002/generalticket/create";

    private int pullSize = 2;

    public void generateEvents(int eventsNumber, String topicName, String ticketName) {

        for (int i = 0; i < pullSize; i++) {

            MessageSender restMessageSender = new MessageSender(
                    ticketRestSender, eventsNumber,
                    1, ticketName,
                    targetUrl, "restSender"
            );


            MessageSender kafkaMessageSender = new MessageSender(
                    ticketProducerSender, eventsNumber,
                    1, ticketName,
                    topicName, "kafkaSender"
            );

            restMessageSender.start();
            kafkaMessageSender.start();
        }

        System.out.println("all events are sent");
    }
}

class MessageSender extends Thread {
    private TicketSender ticketSender;
    private int eventsNumber;
    private int incrementNumber;
    private String ticketName;
    private String destination;
    private String senderName;

    public MessageSender(TicketSender ticketSender,
                         int eventsNumber,
                         int incrementNumber,
                         String ticketName,
                         String destination,
                         String senderName) {
        this.ticketSender = ticketSender;
        this.eventsNumber = eventsNumber;
        this.incrementNumber = incrementNumber;
        this.ticketName = ticketName;
        this.destination = destination;
        this.senderName = senderName;

    }

    @Override
    public void run() {
        System.out.println(ticketSender.getSenderType() + " starting");
        for (int i = 0; i < this.eventsNumber; i++) {
            int ran = incrementNumber;
            ticketSender.sendTicket(destination, new Ticket(ran, ticketName));
            System.out.println("ticket " + ticketName + " incerment " + incrementNumber + " from " + ticketSender.getSenderType() + "count" + ticketSender.getCount());
        }
        System.out.println(ticketSender.getSenderType() + " is done");

    }
}