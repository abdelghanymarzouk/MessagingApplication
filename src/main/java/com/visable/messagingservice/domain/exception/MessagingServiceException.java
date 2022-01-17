package com.visable.messagingservice.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MessagingServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus statusCode;
    private final String errorMessage;

    public MessagingServiceException(HttpStatus httpStatus, String errorMessage){
        super(errorMessage);
        this.statusCode = httpStatus;
        this.errorMessage = errorMessage;

    }

}
