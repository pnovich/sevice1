package com.example.service1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.util.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka
        (partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class TicketProducerSenderTest {
    private final String topticName = "all_tickets5";

    private String bootstrapAddress = "localhost:9092";
    private final String groupId = "allTicketsGroup3";


    @Autowired
    TicketProducerSender ticketProducer;

    @Test
    public void testTicketProducerSender() {

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TicketDTO.class);

        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configProps.put(JsonDeserializer.TYPE_MAPPINGS, "ticketMessage:com.example.service1.TicketDTO, ticketMessage:com.example.service1.Ticket");

//        configProps.put(JsonDeserializer.TYPE_MAPPINGS, "ticketMessage:com.example.service1.TicketDTO, ticketMessage:com.example.service1.Ticket");
//        configProps.put(JsonDeserializer.REMOVE_TYPE_INFO_HEADERS, "true");

        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String,TicketDTO> kafkaConsumer = new KafkaConsumer<String, TicketDTO>(configProps);

        kafkaConsumer.subscribe(Arrays.asList(topticName));

        Ticket fakeTicket1 = new Ticket(007, "James Bond");
        Ticket fakeTicket2 = new Ticket( 001, "Betman");
        ticketProducer.sendTicket(topticName, fakeTicket1);
        ticketProducer.sendTicket(topticName, fakeTicket2);

        System.out.println("ticket sent => " + fakeTicket1);

        ConsumerRecords<String, TicketDTO> records = kafkaConsumer.poll(Duration.ofMillis(10000L));
        System.out.println("records count = " + records.count());
        kafkaConsumer.close();

        Assertions.assertEquals(2, records.count());
        List<Ticket> list = new ArrayList<>();
        records.forEach(r -> {
            Object ticketDto = r.value();
            if (ticketDto instanceof Ticket) {
                list.add((Ticket)ticketDto);
            }
        });

        list.forEach(e -> System.out.println("received element => " + e));
        Assertions.assertEquals(fakeTicket1.getTitle(), list.get(0).getTitle());
    }

}
