package com.visable.messagingservice.domain.service;

import com.visable.messagingservice.model.MessageDto;
import com.visable.messagingservice.model.MessagesDto;
import com.visable.messagingservice.model.PaginationDto;
import com.visable.messagingservice.model.UserDto;
import com.visable.messagingservice.domain.exception.MessagingServiceException;
import com.visable.messagingservice.domain.mapper.MessageMapper;
import com.visable.messagingservice.domain.mapper.UserMapper;
import com.visable.messagingservice.domain.model.MessageEntity;
import com.visable.messagingservice.domain.model.UserEntity;
import com.visable.messagingservice.domain.repository.MessagingRepository;
import com.visable.messagingservice.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.visable.messagingservice.domain.util.Constants.MAX_PAGE_SIZE;
import static com.visable.messagingservice.domain.util.Constants.TOPIC_NAME;

@Service
@Slf4j
public class MessagingService {

    @Autowired
    MessagingRepository messagingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    KafkaTemplate<String, MessageDto> kafkaTemplate;

    public void sendMessage(UUID userId, MessageDto messageDto) {
        if(userId.equals(messageDto.getSentTo())){
            throw new MessagingServiceException(HttpStatus.BAD_REQUEST, "You cannot send a message to yourself");
        }
        messageDto.setSentBy(userId);
        messageDto.sentOn(LocalDateTime.now());
        kafkaTemplate.send(TOPIC_NAME,messageDto);
    }

    public UUID createUser(UserDto userDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByNickName(userDto.getNickName());
        if(userEntityOptional.isPresent()){
            throw new MessagingServiceException(HttpStatus.BAD_REQUEST, "Nick Name already exist");
        }
        UserEntity userEntity = UserMapper.dtoToEntity(userDto);
        UserEntity user = userRepository.save(userEntity);
        return user.getUserId();
    }

    public MessagesDto getReceivedMessages(UUID userId, Integer pageNumber, Integer pageSize) {
        validatePageNumberAndPageSize(pageNumber, pageSize);

        Specification<MessageEntity> sendTo =
                (messageEntity, query, criteriaBuilder) ->
                        criteriaBuilder.equal(messageEntity.get("receiver").get("userId"), userId);
        pageNumber=pageNumber-1;
        Page<MessageEntity> messageEntityPage = messagingRepository.findAll(sendTo, PageRequest.of(pageNumber, pageSize));
        return mapAndPrepareResponse(pageNumber,messageEntityPage);
    }

    public MessagesDto getReceivedMessagesFromParticularUser(UUID currentUserId, UUID userIdToSearchWith, Integer pageNumber, Integer pageSize) {
        validatePageNumberAndPageSize(pageNumber, pageSize);
        if(currentUserId.equals(userIdToSearchWith)){
            throw new MessagingServiceException(HttpStatus.BAD_REQUEST, "User Id you used to search with cannot be your user id");
        }
        Specification<MessageEntity> sendTo =
                (messageEntity, query, criteriaBuilder) ->
                        criteriaBuilder.equal(messageEntity.get("receiver").get("userId"), currentUserId);

        Specification<MessageEntity> sendBy =
                (messageEntity, query, criteriaBuilder) ->
                        criteriaBuilder.equal(messageEntity.get("sender").get("userId"), userIdToSearchWith);

        pageNumber=pageNumber-1;

        Page<MessageEntity> messageEntityPage = messagingRepository
                .findAll(Specification.where(sendTo).and(sendBy), PageRequest.of(pageNumber,pageSize));

        return mapAndPrepareResponse(pageNumber,messageEntityPage);
    }

    public MessagesDto getSentMessages(UUID userId, Integer pageNumber, Integer pageSize) {
        validatePageNumberAndPageSize(pageNumber, pageSize);

        Specification<MessageEntity> sendBy =
                (messageEntity, query, criteriaBuilder) ->
                        criteriaBuilder.equal(messageEntity.get("sender").get("userId"), userId);

        pageNumber=pageNumber-1;

        Page<MessageEntity> messageEntityPage = messagingRepository
                .findAll(sendBy, PageRequest.of(pageNumber,pageSize));

        return  mapAndPrepareResponse(pageNumber,messageEntityPage);
    }

    private void validatePageNumberAndPageSize(Integer pageNumber, Integer pageSize) {
        if (pageNumber != null && pageNumber < 1) {
            throw new MessagingServiceException(HttpStatus.BAD_REQUEST, "Page number must be greater than or equal to 1");
        }

        if (pageSize != null && pageSize > MAX_PAGE_SIZE) {
            throw new MessagingServiceException(HttpStatus.BAD_REQUEST, "Page size must be less than or equal to " + MAX_PAGE_SIZE);
        }
    }

    private MessagesDto mapAndPrepareResponse(Integer pageNumber, Page<MessageEntity> messageEntityPage) {
        List<MessageDto> messageDtoList = new ArrayList<>();
        if (!messageEntityPage.isEmpty()) {
            messageEntityPage.forEach(messageEntity -> messageDtoList.add(MessageMapper.entityToDto(messageEntity)));
        }
        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPageNumber(pageNumber+1);
        paginationDto.setPageSize(messageEntityPage.getSize());
        paginationDto.setTotal(messageEntityPage.getTotalPages());
        MessagesDto messagesDto = new MessagesDto();
        messagesDto.setPagination(paginationDto);
        messagesDto.setMessages(messageDtoList);
        return messagesDto;
    }


    public void saveMessage(MessageDto messageDto) {
        MessageEntity messageEntity = MessageMapper.dtoToEntity(messageDto);
        messagingRepository.save(messageEntity);
    }
}
