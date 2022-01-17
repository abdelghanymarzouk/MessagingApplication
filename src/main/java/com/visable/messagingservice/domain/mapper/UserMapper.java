package com.visable.messagingservice.domain.mapper;


import com.visable.messagingService.model.MessageDto;
import com.visable.messagingService.model.UserDto;
import com.visable.messagingservice.domain.model.MessageEntity;
import com.visable.messagingservice.domain.model.UserEntity;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDto entityToDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setNickName(userEntity.getNickName());
        return userDto;
    }

    public static UserEntity dtoToEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setNickName(userDto.getNickName());
        return userEntity;
    }

}

