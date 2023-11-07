package com.example.service1;

public interface TicketSender {
    public void sendTicket(String destination, Ticket ticket);

    public String getSenderType();

    public int getCount();
}
