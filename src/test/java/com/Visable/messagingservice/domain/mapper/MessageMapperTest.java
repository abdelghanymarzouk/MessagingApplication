package com.Visable.messagingservice.domain.mapper;

import com.visable.messagingservice.domain.mapper.MessageMapper;
import com.visable.messagingservice.domain.model.MessageEntity;
import com.visable.messagingservice.domain.model.UserEntity;
import com.visable.messagingservice.model.MessageDto;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * MessageMapper Tester.
 *
 * @author Abdelghany Marzouk
 * @version 1.0
 */
public class MessageMapperTest {


    @Test
    public void testEntityToDto() {
        UserEntity receiver = new UserEntity() ;
        receiver.setUserId(UUID.randomUUID());
        UserEntity sender = new UserEntity() ;
        sender.setUserId(UUID.randomUUID());
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSender(sender);
        messageEntity.setReceiver(receiver);
        messageEntity.setContent("hello");
        messageEntity.setSentOn(LocalDateTime.now());
        MessageDto messageDto = MessageMapper.entityToDto(messageEntity);
        Assertions.assertEquals(messageDto.getContent(), messageEntity.getContent());
        Assertions.assertEquals(messageDto.getSentBy(), messageEntity.getSender().getUserId());
        Assertions.assertEquals(messageDto.getSentTo(), messageEntity.getReceiver().getUserId());
        Assertions.assertEquals(messageDto.getSentOn(), messageEntity.getSentOn());
    }

    @Test
    public void testDtoToEntity() {
        MessageDto messageDto = new MessageDto();
        messageDto.setSentTo(UUID.randomUUID());
        messageDto.setSentBy(UUID.randomUUID());
        messageDto.setContent("hello");
        messageDto.setSentOn(LocalDateTime.now());
        MessageEntity messageEntity = MessageMapper.dtoToEntity(messageDto);
        Assertions.assertEquals(messageEntity.getContent() ,messageDto.getContent());
        Assertions.assertEquals(messageEntity.getSender().getUserId() ,messageDto.getSentBy());
        Assertions.assertEquals(messageEntity.getReceiver().getUserId(),messageDto.getSentTo());
        Assertions.assertEquals(messageEntity.getSentOn() ,messageDto.getSentOn());
    }

} 
