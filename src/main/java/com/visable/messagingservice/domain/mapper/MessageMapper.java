package com.visable.messagingservice.domain.mapper;


import com.visable.messagingService.model.MessageDto;
import com.visable.messagingservice.domain.model.MessageEntity;
import com.visable.messagingservice.domain.model.UserEntity;

public class MessageMapper {

    private MessageMapper() {
    }

    public static MessageDto entityToDto(MessageEntity messageEntity) {
        MessageDto messageDto = new MessageDto();
        messageDto.sentOn(messageEntity.getSentOn());
        messageDto.setSentBy(messageEntity.getSender().getUserId());
        messageDto.setSentTo(messageEntity.getReceiver().getUserId());
        messageDto.setContent(messageEntity.getContent());
        return messageDto;
    }

    public static MessageEntity dtoToEntity(MessageDto messageDto) {
        MessageEntity messageEntity = new MessageEntity();
        UserEntity receiver = new UserEntity();
        receiver.setUserId(messageDto.getSentTo());
        messageEntity.setReceiver(receiver);
        UserEntity sender = new UserEntity();
        sender.setUserId(messageDto.getSentBy());
        messageEntity.setSender(sender);
        messageEntity.setContent(messageDto.getContent());
        messageEntity.setSentOn(messageDto.getSentOn());
        return messageEntity;
    }

}

