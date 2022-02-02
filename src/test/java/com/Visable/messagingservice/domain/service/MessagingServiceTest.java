package com.Visable.messagingservice.domain.service;

import com.visable.messagingservice.domain.exception.MessagingServiceException;
import com.visable.messagingservice.domain.model.MessageEntity;
import com.visable.messagingservice.domain.model.UserEntity;
import com.visable.messagingservice.domain.repository.MessagingRepository;
import com.visable.messagingservice.domain.repository.UserRepository;
import com.visable.messagingservice.domain.service.MessagingService;
import com.visable.messagingservice.model.MessageDto;
import com.visable.messagingservice.model.MessagesDto;
import com.visable.messagingservice.model.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * MessagingService Tester.
 *
 * @author Abdelghany Marzouk
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class MessagingServiceTest {

    @InjectMocks
    MessagingService messagingService;

    @Mock
    MessagingRepository messagingRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    KafkaTemplate<String, MessageDto> kafkaTemplate;

    @Test
    void testGetReceivedMessagesFromParticularUser() {
        int pageNumber = 1, pageSize = 10;
        UUID receiverUserId = UUID.randomUUID();
        UUID userIdToSearchBy = UUID.randomUUID();
        UserEntity receiver = new UserEntity() ;
        receiver.setUserId(receiverUserId);
        UserEntity sender = new UserEntity() ;
        sender.setUserId(userIdToSearchBy);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSender(sender);
        messageEntity.setReceiver(receiver);
        messageEntity.setContent("hello");
        messageEntity.setSentOn(LocalDateTime.now());
        Page<MessageEntity> messageEntityPage = new PageImpl<>(Collections.singletonList(messageEntity));
        when(messagingRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(messageEntityPage);
        MessagesDto messagesDto = messagingService.getReceivedMessagesFromParticularUser(receiverUserId,userIdToSearchBy,pageNumber, pageSize);
        Assertions.assertEquals(messageEntityPage.getTotalPages(),
                messagesDto.getPagination().getTotal());
        Assertions.assertEquals(pageNumber, messagesDto.getPagination().getPageNumber());
        Assertions.assertEquals(messageEntityPage.getSize(), messagesDto.getPagination().getPageSize());
        Assertions.assertEquals(messageEntityPage.getContent().size(),
                messagesDto.getMessages().size());
        Assertions.assertEquals(userIdToSearchBy,
                messagesDto.getMessages().get(0).getSentBy());
        Assertions.assertEquals(receiverUserId,
                messagesDto.getMessages().get(0).getSentTo());
        Assertions.assertEquals(messageEntity.getContent(),
                messagesDto.getMessages().get(0).getContent());
    }

    @Test
    void testGetReceivedMessagesFromParticularUser_SearchByUserIdIsEqualsYourUserId() {
        int pageNumber = 1, pageSize = 10;
        UUID userId = UUID.randomUUID();
        MessagingServiceException thrown = assertThrows(MessagingServiceException.class,
                () -> messagingService.getReceivedMessagesFromParticularUser(userId,userId,pageNumber, pageSize));
        assertNotNull(thrown);
    }

    @Test
    void testGetReceivedMessagesFromParticularUser_InvalidPageNumber() {
        int pageNumber = -1, pageSize = 10;
        MessagingServiceException thrown = assertThrows(MessagingServiceException.class,
                () -> messagingService.getReceivedMessagesFromParticularUser(UUID.randomUUID(),UUID.randomUUID(),pageNumber, pageSize));
        assertNotNull(thrown);
    }

    @Test
    void testGetReceivedMessagesFromParticularUser_InvalidPageSize() {
        int pageNumber = 1, pageSize = 1000;
        MessagingServiceException thrown = assertThrows(MessagingServiceException.class,
                () -> messagingService.getReceivedMessagesFromParticularUser(UUID.randomUUID(),UUID.randomUUID(),pageNumber, pageSize));
        assertNotNull(thrown);
    }

    @Test
    void testGetReceivedMessagesFromParticularUser_EmptyResult() {
        int pageNumber = 1, pageSize = 10;
        Page<MessageEntity> messageEntityPage = new PageImpl<>(new ArrayList<>());
        when(messagingRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(messageEntityPage);
        MessagesDto messagesDto = messagingService.getReceivedMessagesFromParticularUser(UUID.randomUUID(),UUID.randomUUID(),pageNumber, pageSize);
        Assertions.assertEquals(messageEntityPage.getTotalPages(),
                messagesDto.getPagination().getTotal());
        Assertions.assertEquals(pageNumber, messagesDto.getPagination().getPageNumber());
        Assertions.assertEquals(messageEntityPage.getSize(), messagesDto.getPagination().getPageSize());
        Assertions.assertEquals(messageEntityPage.getContent().size(),
                messagesDto.getMessages().size());
    }

    @Test
    void testGetReceivedMessages() {
        int pageNumber = 1, pageSize = 10;
        UUID receiverUserId = UUID.randomUUID();
        UserEntity receiver = new UserEntity() ;
        receiver.setUserId(receiverUserId);
        UserEntity sender = new UserEntity() ;
        sender.setUserId(UUID.randomUUID());
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSender(sender);
        messageEntity.setReceiver(receiver);
        messageEntity.setContent("hello");
        messageEntity.setSentOn(LocalDateTime.now());
        Page<MessageEntity> messageEntityPage = new PageImpl<>(Collections.singletonList(messageEntity));
        when(messagingRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(messageEntityPage);
        MessagesDto messagesDto = messagingService.getReceivedMessages(receiverUserId,pageNumber, pageSize);
        Assertions.assertEquals(messageEntityPage.getTotalPages(),
            messagesDto.getPagination().getTotal());
        Assertions.assertEquals(pageNumber, messagesDto.getPagination().getPageNumber());
        Assertions.assertEquals(messageEntityPage.getSize(), messagesDto.getPagination().getPageSize());
        Assertions.assertEquals(messageEntityPage.getContent().size(),
            messagesDto.getMessages().size());
        Assertions.assertEquals(receiverUserId,
                messagesDto.getMessages().get(0).getSentTo());
        Assertions.assertEquals(messageEntity.getContent(),
                messagesDto.getMessages().get(0).getContent());


    }

    @Test
    void testGetReceivedMessages_InvalidPageNumber() {
        int pageNumber = -1, pageSize = 10;
        MessagingServiceException thrown = assertThrows(MessagingServiceException.class,
                () -> messagingService.getReceivedMessages(UUID.randomUUID(),pageNumber, pageSize));
        assertNotNull(thrown);
    }

    @Test
    void testGetReceivedMessages_InvalidPageSize() {
        int pageNumber = 1, pageSize = 1000;
        MessagingServiceException thrown = assertThrows(MessagingServiceException.class,
                () -> messagingService.getReceivedMessages(UUID.randomUUID(),pageNumber, pageSize));
        assertNotNull(thrown);
    }

    @Test
    void testGetReceivedMessages_EmptyResult() {
        int pageNumber = 1, pageSize = 10;
        Page<MessageEntity> messageEntityPage = new PageImpl<>(new ArrayList<>());
        when(messagingRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(messageEntityPage);
        MessagesDto messagesDto = messagingService.getReceivedMessages(UUID.randomUUID(),pageNumber, pageSize);
        Assertions.assertEquals(messageEntityPage.getTotalPages(),
                messagesDto.getPagination().getTotal());
        Assertions.assertEquals(pageNumber, messagesDto.getPagination().getPageNumber());
        Assertions.assertEquals(messageEntityPage.getSize(), messagesDto.getPagination().getPageSize());
        Assertions.assertEquals(messageEntityPage.getContent().size(),
                messagesDto.getMessages().size());
    }


    @Test
    void testGetSentMessages() {
        int pageNumber = 1, pageSize = 10;
        UUID senderUserId = UUID.randomUUID();
        UserEntity receiver = new UserEntity() ;
        receiver.setUserId(UUID.randomUUID());
        UserEntity sender = new UserEntity() ;
        sender.setUserId(senderUserId);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSender(sender);
        messageEntity.setReceiver(receiver);
        messageEntity.setContent("hello");
        messageEntity.setSentOn(LocalDateTime.now());
        Page<MessageEntity> messageEntityPage = new PageImpl<>(Collections.singletonList(messageEntity));
        when(messagingRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(messageEntityPage);
        MessagesDto messagesDto = messagingService.getSentMessages(senderUserId,pageNumber, pageSize);
        Assertions.assertEquals(messageEntityPage.getTotalPages(),
                messagesDto.getPagination().getTotal());
        Assertions.assertEquals(pageNumber, messagesDto.getPagination().getPageNumber());
        Assertions.assertEquals(messageEntityPage.getSize(), messagesDto.getPagination().getPageSize());
        Assertions.assertEquals(messageEntityPage.getContent().size(),
                messagesDto.getMessages().size());
        Assertions.assertEquals(senderUserId,
                messagesDto.getMessages().get(0).getSentBy());
        Assertions.assertEquals(messageEntity.getContent(),
                messagesDto.getMessages().get(0).getContent());
    }

    @Test
    void testGetSentMessages_InvalidPageNumber() {
        int pageNumber = -1, pageSize = 10;
        MessagingServiceException thrown = assertThrows(MessagingServiceException.class,
            () -> messagingService.getSentMessages(UUID.randomUUID(),pageNumber, pageSize));
        assertNotNull(thrown);
    }

    @Test
    void testGetSentMessages_InvalidPageSize() {
        int pageNumber = 1, pageSize = 1000;
        MessagingServiceException thrown = assertThrows(MessagingServiceException.class,
                () -> messagingService.getSentMessages(UUID.randomUUID(),pageNumber, pageSize));
        assertNotNull(thrown);
    }

    @Test
    void testGetSentMessages_EmptyResult() {
        int pageNumber = 1, pageSize = 10;
        Page<MessageEntity> messageEntityPage = new PageImpl<>(new ArrayList<>());
        when(messagingRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(messageEntityPage);
        MessagesDto messagesDto = messagingService.getSentMessages(UUID.randomUUID(),pageNumber, pageSize);
        Assertions.assertEquals(messageEntityPage.getTotalPages(),
            messagesDto.getPagination().getTotal());
        Assertions.assertEquals(pageNumber, messagesDto.getPagination().getPageNumber());
        Assertions.assertEquals(messageEntityPage.getSize(), messagesDto.getPagination().getPageSize());
        Assertions.assertEquals(messageEntityPage.getContent().size(),
            messagesDto.getMessages().size());
    }

    @Test
    void testCreateUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID());
        when(userRepository.findByNickName(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(userEntity);
        UUID userId = messagingService.createUser(new UserDto());
        Assertions.assertNotNull(userId);
    }

    @Test
    void testCreateUser_WithExistNickName() {
        when(userRepository.findByNickName(any())).thenReturn(Optional.of(new UserEntity()));
        MessagingServiceException thrown = assertThrows(MessagingServiceException.class,
                () -> messagingService.createUser(new UserDto()));
        assertNotNull(thrown);
    }

    @Test
    void testSendMessage() {
        when(kafkaTemplate.send(any(),any())).thenReturn(new SettableListenableFuture());
        Assertions.assertDoesNotThrow(()->messagingService.sendMessage(UUID.randomUUID(),new MessageDto()));
    }

    @Test
    void testSendMessage_SendMessageToYourself() {
        UUID userId = UUID.randomUUID();
        MessageDto messageDto = new MessageDto();
        messageDto.setSentTo(userId);
        MessagingServiceException thrown = assertThrows(MessagingServiceException.class,
                () -> messagingService.sendMessage(userId,messageDto));
        assertNotNull(thrown);
    }

} 
