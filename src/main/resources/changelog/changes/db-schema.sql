--liquibase formatted sql


create table if not exists Messages
(
    message_id                 uuid         not null
        constraint message_pkey
            primary key,
    content      varchar(255) not null,
    sent_On       varchar(255) not null,
    sent_by uuid
        constraint fk_user_sender
            references sender,
    sent_to uuid
    constraint fk_user_receiver
    references receiver
);

create table if not exists users
(
    user_id                       uuid         not null
        constraint user_pkey
            primary key,
    nick_name             varchar(255) not null,
);
