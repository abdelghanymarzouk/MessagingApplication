package com.visable.messagingservice.domain.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

@Entity(name = "Users")
public class UserEntity {

    @Id
    @Column(name = "user_id",nullable = false, updatable = false)
    @GeneratedValue
    private UUID userId;

    @Column(name = "nick_name",nullable = false)
    private String nickName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy = "receiver")
    private List<MessageEntity> sentMessages;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy = "sender")
    private List<MessageEntity> receivedMessages;
}
