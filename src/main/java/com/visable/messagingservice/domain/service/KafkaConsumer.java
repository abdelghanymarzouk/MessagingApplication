package com.visable.messagingservice.domain.service;


import com.visable.messagingService.model.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.visable.messagingservice.domain.util.Constants.GROUP_ID;
import static com.visable.messagingservice.domain.util.Constants.TOPIC_NAME;

@Component
public class KafkaConsumer {

    @Autowired
    MessagingService messagingService;

    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID,
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(MessageDto messageDto) {
        System.out.println("Message Consumed "+messageDto.getContent()+"\n from " + messageDto.getSentBy());
        messagingService.saveMessage(messageDto);
    }
}
