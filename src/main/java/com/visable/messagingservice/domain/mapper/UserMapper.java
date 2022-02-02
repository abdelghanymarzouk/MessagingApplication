package com.visable.messagingservice.domain.mapper;


import com.visable.messagingservice.model.UserDto;
import com.visable.messagingservice.domain.model.UserEntity;

public class UserMapper {

    private UserMapper() {
    }

    public static UserEntity dtoToEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setNickName(userDto.getNickName());
        return userEntity;
    }

}

