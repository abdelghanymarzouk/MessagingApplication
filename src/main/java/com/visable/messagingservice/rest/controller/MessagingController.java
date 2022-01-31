package com.visable.messagingservice.rest.controller;

import com.visable.messagingservice.controller.MessagingServiceApiDelegate;
import com.visable.messagingservice.model.MessageDto;
import com.visable.messagingservice.model.MessagesDto;
import com.visable.messagingservice.model.UserDto;
import com.visable.messagingservice.domain.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
public class MessagingController implements MessagingServiceApiDelegate {

    @Autowired
    MessagingService messagingService;

    @Override
    public ResponseEntity<Void> createUser(UserDto userDto) {
        messagingService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<MessagesDto> getReceivedMessages(UUID userId, Integer pageNumber, Integer pageSize) {
        MessagesDto messagesDto = messagingService.getReceivedMessages(userId,pageNumber,pageSize);
        return ResponseEntity.ok(messagesDto);
    }

    @Override
    public ResponseEntity<MessagesDto> getReceivedMessagesFromParticularUser(UUID currentUserId, UUID userIdToSearchBy, Integer pageNumber, Integer pageSize) {
        MessagesDto messagesDto = messagingService.getReceivedMessagesFromParticularUser(currentUserId,userIdToSearchBy,pageNumber,pageSize);
        return ResponseEntity.ok(messagesDto);
    }


    @Override
    public ResponseEntity<MessagesDto> getSentMessages(UUID userId, Integer pageNumber, Integer pageSize) {
        MessagesDto messagesDto = messagingService.getSentMessages(userId,pageNumber,pageSize);
        return ResponseEntity.ok(messagesDto);
    }

    @Override
    public ResponseEntity<Void> sendMessage(UUID userId, MessageDto messageDto) {
        messagingService.sendMessage(userId,messageDto);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
