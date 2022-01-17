package com.visable.messagingservice.domain.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter

@Entity(name = "Messages")
public class MessageEntity {

    @Id
    @Column(name = "message_id",nullable = false, updatable = false)
    @GeneratedValue
    private UUID messageId;

    @Column(name = "content",nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name="sent_to")
    private UserEntity receiver;

    @ManyToOne
    @JoinColumn(name="sent_by")
    private UserEntity sender;

    @Column(name = "sent_on",nullable = false)
    private LocalDateTime sentOn;

}
