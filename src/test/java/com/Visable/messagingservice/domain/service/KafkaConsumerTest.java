package com.Visable.messagingservice.domain.service;


import com.visable.messagingservice.MessagingServiceApplication;
import com.visable.messagingservice.domain.model.MessageEntity;
import com.visable.messagingservice.domain.repository.MessagingRepository;
import com.visable.messagingservice.domain.service.KafkaConsumer;
import com.visable.messagingservice.model.MessageDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.visable.messagingservice.domain.util.Constants.TOPIC_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;



@SpringBootTest(classes = { MessagingServiceApplication.class })
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@ActiveProfiles(value = "test")
class KafkaConsumerTest {


    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    public KafkaTemplate<String, MessageDto> template;

    @MockBean
    private MessagingRepository messagingRepository;

    @Test
    public void testKafkaConsumer()
            throws Exception {
        MessageDto messageDto = new MessageDto();
        messageDto.setContent("hello world");
        messageDto.setSentTo(UUID.randomUUID());
        doReturn(new MessageEntity()).when(messagingRepository).save(any());
        template.send(TOPIC_NAME, messageDto);
        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(consumer.getLatch().getCount(), equalTo(0L));
        assertThat(consumer.getPayload(), containsString("hello world"));
    }


}
