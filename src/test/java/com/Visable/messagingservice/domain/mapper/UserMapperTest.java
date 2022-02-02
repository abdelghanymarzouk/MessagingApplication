package com.Visable.messagingservice.domain.mapper;

import com.visable.messagingservice.domain.mapper.MessageMapper;
import com.visable.messagingservice.domain.mapper.UserMapper;
import com.visable.messagingservice.domain.model.MessageEntity;
import com.visable.messagingservice.domain.model.UserEntity;
import com.visable.messagingservice.model.MessageDto;
import com.visable.messagingservice.model.UserDto;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * UserMapper Tester.
 *
 * @author Abdelghany Marzouk
 * @version 1.0
 */
public class UserMapperTest {


    @Test
    public void testDtoToEntity() {
        UserDto userDto = new UserDto();
        userDto.setNickName("hassan");
        UserEntity userEntity = UserMapper.dtoToEntity(userDto);
        Assertions.assertEquals(userEntity.getNickName() ,userDto.getNickName());
    }

} 
