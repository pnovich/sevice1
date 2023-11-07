package com.example.service1;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TicketRestSender implements TicketSender {

    private String senderType = "rest sender";

    private volatile int restCount;

    @Override
    public void sendTicket(String url, Ticket ticket) {
        RestTemplate restTemplate = new RestTemplate();
        String createPersonUrl = url;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("number", ticket.getNumber());
        jsonObject.put("title", ticket.getTitle());

        HttpEntity<String> request =
                new HttpEntity<String>(jsonObject.toString(), headers);
        ResponseEntity<String> responseEntityStr = restTemplate.
                postForEntity(createPersonUrl, request, String.class);
        restCount++;

    }

    @Override
    public String getSenderType() {
        return this.senderType;
    }

    @Override
    public int getCount() {
        return restCount;
    }
}
