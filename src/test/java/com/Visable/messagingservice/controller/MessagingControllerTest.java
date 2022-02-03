package com.Visable.messagingservice.controller;

import com.Visable.messagingservice.configuration.BasePostgresqlContainer;
import com.visable.messagingservice.MessagingServiceApplication;
import com.visable.messagingservice.domain.exception.MessagingServiceException;
import com.visable.messagingservice.domain.service.MessagingService;
import com.visable.messagingservice.model.MessageDto;
import com.visable.messagingservice.model.MessagesDto;
import com.visable.messagingservice.model.PaginationDto;
import com.visable.messagingservice.model.UserDto;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
class MessagingControllerTest extends BasePostgresqlContainer{


    private static final String GET_SENT_MESSAGES_ENDPOINT = "/api/messaging-service/getSentMessages";
    private static final String GET_RECEIVED_MESSAGES_ENDPOINT = "/api/messaging-service/getReceivedMessages";
    private static final String GET_RECEIVED_MESSAGES_FROM_PARTICULAR_USER_ENDPOINT = "/api/messaging-service/getReceivedMessagesFromParticularUser/{userIdToSearchBy}";
    private static final String POST_CREATE_USER_ENDPOINT = "/api/messaging-service/user";
    private static final String POST_SEND_MESSAGE_ENDPOINT = "/api/messaging-service/sendMessage";

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MessagingService messagingService;

    @Autowired
    private MockMvc mvc;

    @Test
    void testGetReceivedMessages() {
        Map<String, Integer> parametersMap = new HashMap<>();
        parametersMap.put("pageSize", 10);
        parametersMap.put("pageNumber", 1);
        MessagesDto messagesDto = new MessagesDto();
        messagesDto.setPagination(new PaginationDto().total(1));
        doReturn(messagesDto).when(messagingService).getReceivedMessages(any(),any(),any());
        given().mockMvc(mvc).log().ifValidationFails().queryParams(parametersMap).header("userId",UUID.randomUUID()).contentType(JSON)
                .when()
                .get(GET_RECEIVED_MESSAGES_ENDPOINT)
                .then().log().ifValidationFails().statusCode(OK.value()).contentType(JSON);

        verify(messagingService, times(1)).getReceivedMessages(any(),any(),any());
    }

    @Test
    void testGetReceivedMessagesFromParticularUser() {
        Map<String, Integer> parametersMap = new HashMap<>();
        parametersMap.put("pageSize", 10);
        parametersMap.put("pageNumber", 1);
        MessagesDto messagesDto = new MessagesDto();
        messagesDto.setPagination(new PaginationDto().total(1));
        doReturn(messagesDto).when(messagingService).getReceivedMessagesFromParticularUser(any(),any(),any(),any());
        given().mockMvc(mvc).log().ifValidationFails().queryParams(parametersMap).header("userId",UUID.randomUUID()).contentType(JSON)
                .when()
                .get(GET_RECEIVED_MESSAGES_FROM_PARTICULAR_USER_ENDPOINT,UUID.randomUUID())
                .then().log().ifValidationFails().statusCode(OK.value()).contentType(JSON);

        verify(messagingService, times(1)).getReceivedMessagesFromParticularUser(any(),any(),any(),any());
    }
    @Test
    void testGetSentMessages() {
        Map<String, Integer> parametersMap = new HashMap<>();
        parametersMap.put("pageSize", 10);
        parametersMap.put("pageNumber", 1);
        MessagesDto messagesDto = new MessagesDto();
        messagesDto.setPagination(new PaginationDto().total(1));
        doReturn(messagesDto).when(messagingService).getSentMessages(any(),any(),any());
        given().mockMvc(mvc).log().ifValidationFails().queryParams(parametersMap).header("userId",UUID.randomUUID()).contentType(JSON)
                .when()
                .get(GET_SENT_MESSAGES_ENDPOINT)
                .then().log().ifValidationFails().statusCode(OK.value()).contentType(JSON);

        verify(messagingService, times(1)).getSentMessages(any(),any(),any());
    }


    @Test
    void testCreateUser() {
        doReturn(UUID.randomUUID()).when(messagingService).createUser(any());
        given().mockMvc(mvc).log().ifValidationFails().body(new UserDto()).contentType(JSON)
            .when()
            .post(POST_CREATE_USER_ENDPOINT)
            .then().log().ifValidationFails().statusCode(CREATED.value()).contentType(JSON);

        verify(messagingService, times(1)).createUser(any());
    }
    @Test
    void testCreateUser_ThrowsException() {
        doThrow(new MessagingServiceException(BAD_REQUEST,"exception")).when(messagingService).createUser(any());
        given().mockMvc(mvc).log().ifValidationFails().body(new UserDto()).contentType(JSON)
                .when()
                .post(POST_CREATE_USER_ENDPOINT)
                .then().log().ifValidationFails().statusCode(BAD_REQUEST.value()).contentType(JSON);

        verify(messagingService, times(1)).createUser(any());
    }

    @Test
    void testSendMessage() {
        doNothing().when(messagingService).sendMessage(any(),any());
        given().mockMvc(mvc).log().ifValidationFails().body(new MessageDto()).header("userId",UUID.randomUUID()).contentType(JSON)
                .when()
                .post(POST_SEND_MESSAGE_ENDPOINT)
                .then().log().ifValidationFails().statusCode(NO_CONTENT.value());

        verify(messagingService, times(1)).sendMessage(any(),any());
    }

    @Test
    void testSendMessage_ThrowsException() {
        doThrow(new MessagingServiceException(BAD_REQUEST,"exception")).when(messagingService).sendMessage(any(),any());
        given().mockMvc(mvc).log().ifValidationFails().body(new MessageDto()).header("userId",UUID.randomUUID()).contentType(JSON)
                .when()
                .post(POST_SEND_MESSAGE_ENDPOINT)
                .then().log().ifValidationFails().statusCode(BAD_REQUEST.value()).contentType(JSON);

        verify(messagingService, times(1)).sendMessage(any(),any());
    }

} 
