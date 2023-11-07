package com.example.service1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Service1Controller {
    @Autowired
    TicketService ticketService;

    @GetMapping("")
    public String getDefault() {
        return "default";
    }

    @GetMapping("/tickets/ticket1/{number}")
    public String createTicket1WithNumber(@PathVariable("number") int number) throws Exception {
        ticketService.generateEvents(number, "all_tickets2", "ticket1");
        System.out.println("ticket sent");
        return "generated " + number + " tickets with title ticket1 ";
    }

    @GetMapping("/tickets/ticket2/{number}")
    public String createTicket2WithNumber(@PathVariable("number") int number) throws Exception {
        ticketService.generateEvents(number, "all_tickets2", "ticket2");
        System.out.println("ticket sent");
        return "generated " + number + " tickets with title ticket2 ";
    }


    @PostMapping("/tickets")
    public Ticket createTicket(@RequestBody Ticket ticket) {
        return new Ticket(0, "null ticket");
    }

}
