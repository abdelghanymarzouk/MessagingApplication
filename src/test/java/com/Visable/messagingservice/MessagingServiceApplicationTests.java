package com.Visable.messagingservice;

import com.Visable.messagingservice.configuration.BasePostgresqlContainer;
import com.visable.messagingservice.MessagingServiceApplication;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(classes = MessagingServiceApplication.class)
@ActiveProfiles(value = "test")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class MessagingServiceApplicationTests extends BasePostgresqlContainer{

	@Test
	void contextLoads() {
	}

}
