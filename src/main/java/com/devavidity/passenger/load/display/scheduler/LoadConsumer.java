package com.devavidity.passenger.load.display.scheduler;

import com.devavidity.passenger.load.display.controller.PassengerLoadController;
import com.devavidity.passenger.load.display.model.Compartment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.ExtendedModelMap;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;


@Component
public class LoadConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedRate = 5000)
    public void consumeMessage() throws IOException {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("kafka_output"));

        PassengerLoadController passengerLoadController = new PassengerLoadController();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                Compartment compartment = objectMapper.readValue(record.value(), Compartment.class);
                String key = compartment.getVehicleId() + "" + compartment.getCompartmentId();
                passengerLoadController.addValue(key, compartment);
            }
        }
    }


}
