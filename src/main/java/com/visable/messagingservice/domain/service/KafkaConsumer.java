package com.visable.messagingservice.domain.service;


import com.visable.messagingservice.model.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

import static com.visable.messagingservice.domain.util.Constants.GROUP_ID;
import static com.visable.messagingservice.domain.util.Constants.TOPIC_NAME;

@Component
public class KafkaConsumer {

    @Autowired
    MessagingService messagingService;

    private CountDownLatch latch = new CountDownLatch(1);

    private String payload = null;

    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID,
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(MessageDto messageDto) {
        System.out.println("Message Consumed "+messageDto.getContent()+"\n from " + messageDto.getSentBy());
        setPayload(messageDto.toString());
        messagingService.saveMessage(messageDto);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}
