package com.Visable.messagingservice.controller;

import com.Visable.messagingservice.configuration.BasePostgresqlContainer;
import com.visable.messagingservice.MessagingServiceApplication;
import com.visable.messagingservice.domain.model.UserEntity;
import com.visable.messagingservice.domain.repository.UserRepository;
import com.visable.messagingservice.domain.service.KafkaConsumer;
import com.visable.messagingservice.domain.service.MessagingService;
import com.visable.messagingservice.model.MessageDto;
import com.visable.messagingservice.model.UserDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.*;

/**
 * MessagingController Tester.
 *
 * @author Abdelghany Marzouk
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = { MessagingServiceApplication.class })
@ActiveProfiles(value = "test")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessagingControllerIT extends BasePostgresqlContainer {


    private static final String GET_SENT_MESSAGES_ENDPOINT = "/api/messaging-service/getSentMessages";
    private static final String GET_RECEIVED_MESSAGES_ENDPOINT = "/api/messaging-service/getReceivedMessages";
    private static final String GET_RECEIVED_MESSAGES_FROM_PARTICULAR_USER_ENDPOINT = "/api/messaging-service/getReceivedMessagesFromParticularUser/{userIdToSearchBy}";
    private static final String POST_CREATE_USER_ENDPOINT = "/api/messaging-service/user";
    private static final String POST_SEND_MESSAGE_ENDPOINT = "/api/messaging-service/sendMessage";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private MockMvc mvc;

    public UUID userId1;
    public UUID userId2;
    public UUID userId3;

    @BeforeAll
    public void setup(){
        UserDto user1 = new UserDto();
        user1.setNickName("Abdelghany");
        UserDto user2 = new UserDto();
        user2.setNickName("Marzouk");
        UserDto user3 = new UserDto();
        user3.setNickName("Mahmoud");
        userId1= messagingService.createUser(user1);
        userId2= messagingService.createUser(user2);
        userId3= messagingService.createUser(user3);
        MessageDto messageDto = new MessageDto();
        messageDto.setContent("Message from Abdelghany to Marzouk");
        messageDto.setSentTo(userId2);
        messagingService.sendMessage(userId1,messageDto);
        messageDto.setContent("Message from Marzouk to Mahmoud");
        messageDto.setSentTo(userId3);
        messagingService.sendMessage(userId2,messageDto);
        messageDto.setContent("Message from Mahmoud to Abdelghany");
        messageDto.setSentTo(userId1);
        messagingService.sendMessage(userId3,messageDto);
    }

    @Test
    void testGetReceivedMessages() {
        Map<String, Integer> parametersMap = new HashMap<>();
        parametersMap.put("pageSize", 10);
        parametersMap.put("pageNumber", 1);
        given().mockMvc(mvc).log().ifValidationFails().queryParams(parametersMap).header("userId",userId1).contentType(JSON)
                .when()
                .get(GET_RECEIVED_MESSAGES_ENDPOINT)
                .then().expect(MockMvcResultMatchers.jsonPath("$.messages").exists())
                .expect(MockMvcResultMatchers.jsonPath("$.messages[0].content").value("Message from Mahmoud to Abdelghany"))
                .expect(MockMvcResultMatchers.jsonPath("$.messages[0].sentBy").value(userId3.toString()))
                .log().ifValidationFails().statusCode(OK.value()).contentType(JSON);
    }

    @Test
    void testGetReceivedMessagesFromParticularUser() {
        Map<String, Integer> parametersMap = new HashMap<>();
        parametersMap.put("pageSize", 10);
        parametersMap.put("pageNumber", 1);
        given().mockMvc(mvc).log().ifValidationFails().queryParams(parametersMap).header("userId",userId2).contentType(JSON)
                .when()
                .get(GET_RECEIVED_MESSAGES_FROM_PARTICULAR_USER_ENDPOINT,userId1)
                .then().expect(MockMvcResultMatchers.jsonPath("$.messages").exists())
                .expect(MockMvcResultMatchers.jsonPath("$.messages[0].content").value("Message from Abdelghany to Marzouk"))
                .expect(MockMvcResultMatchers.jsonPath("$.messages[0].sentBy").value(userId1.toString()))
                .log().ifValidationFails().statusCode(OK.value()).contentType(JSON);
    }

    @Test
    void testGetReceivedMessagesFromParticularUser_BadRequest() {
        Map<String, Integer> parametersMap = new HashMap<>();
        parametersMap.put("pageSize", 10);
        parametersMap.put("pageNumber", 1);
        given().mockMvc(mvc).log().ifValidationFails().queryParams(parametersMap).header("userId",userId2).contentType(JSON)
                .when()
                .get(GET_RECEIVED_MESSAGES_FROM_PARTICULAR_USER_ENDPOINT,userId2)
                .then().log().ifValidationFails().statusCode(BAD_REQUEST.value()).contentType(JSON);
    }

    @Test
    void testGetSentMessages() {
        Map<String, Integer> parametersMap = new HashMap<>();
        parametersMap.put("pageSize", 10);
        parametersMap.put("pageNumber", 1);
        given().mockMvc(mvc).log().ifValidationFails().queryParams(parametersMap).header("userId",userId2).contentType(JSON)
                .when()
                .get(GET_SENT_MESSAGES_ENDPOINT)
                .then().expect(MockMvcResultMatchers.jsonPath("$.messages").exists())
                .expect(MockMvcResultMatchers.jsonPath("$.messages[0].content").value("Message from Marzouk to Mahmoud"))
                .expect(MockMvcResultMatchers.jsonPath("$.messages[0].sentTo").value(userId3.toString()))
                .log().ifValidationFails().statusCode(OK.value()).contentType(JSON);
    }

    @Test
    void testCreateUser() {
        UserDto newUserDto = new UserDto();
        String nickName = "MOHAMED_HASSAN";
        newUserDto.setNickName(nickName);
        given().mockMvc(mvc).log().ifValidationFails().body(newUserDto).contentType(JSON)
            .when()
            .post(POST_CREATE_USER_ENDPOINT)
            .then().log().ifValidationFails().statusCode(CREATED.value()).contentType(JSON);

        Optional<UserEntity> userEntity =  userRepository.findByNickName(nickName);
        assertThat(userEntity.isPresent(), equalTo(true));
        assertThat(userEntity.get().getNickName(), equalTo(nickName));

    }

    @Test
    void testCreateUser_BadRequest() {
        UserDto userDto = new UserDto();
        userDto.setNickName("Abdelghany");
        given().mockMvc(mvc).log().ifValidationFails().body(userDto).contentType(JSON)
                .when()
                .post(POST_CREATE_USER_ENDPOINT)
                .then().log().ifValidationFails().statusCode(BAD_REQUEST.value()).contentType(JSON);

    }

    @Test
    void testSendMessage() throws InterruptedException {
        MessageDto messageDto = new MessageDto();
        messageDto.setContent("new Message");
        messageDto.sentTo(userId2);
        given().mockMvc(mvc).log().ifValidationFails().body(messageDto).header("userId",userId1).contentType(JSON)
                .when()
                .post(POST_SEND_MESSAGE_ENDPOINT)
                .then().log().ifValidationFails().statusCode(NO_CONTENT.value());
        consumer.getLatch().await(5000, TimeUnit.MILLISECONDS);
        assertThat(consumer.getLatch().getCount(), equalTo(0L));
        assertThat(consumer.getPayload(), containsString("new Message"));

    }

    @Test
    void testSendMessage_BadRequest() {
        MessageDto messageDto = new MessageDto();
        messageDto.setContent("Message");
        messageDto.sentTo(userId1);
        given().mockMvc(mvc).log().ifValidationFails().body(messageDto).header("userId",userId1).contentType(JSON)
                .when()
                .post(POST_SEND_MESSAGE_ENDPOINT)
                .then().log().ifValidationFails().statusCode(BAD_REQUEST.value()).contentType(JSON);

    }

} 
