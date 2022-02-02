package com.visable.messagingservice.domain.exception;

import com.visable.messagingservice.model.ProblemDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@ControllerAdvice
public class MessagingExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String TITLE = "Messaging service Error" ;
    @ExceptionHandler(MessagingServiceException.class)
    public ResponseEntity<Object> handleException(MessagingServiceException exception) {

        log.warn("Messaging service Exception with cause : {}", exception.getErrorMessage());
        ProblemDto problem = new ProblemDto();
        problem.setStatus(exception.getStatusCode().value());
        problem.setInstance(exception.getStatusCode().getReasonPhrase());
        problem.setType(exception.getStatusCode().getReasonPhrase());
        problem.setDetail(exception.getErrorMessage());
        problem.setTitle(TITLE);

        return ResponseEntity.status(exception.getStatusCode()).body(problem);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        ProblemDto problem = new ProblemDto();
        problem.setStatus(HttpStatus.BAD_REQUEST.value());
        problem.setInstance(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problem.setType(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problem.setDetail(ex.getMessage());
        problem.setTitle(TITLE);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentExceptionException(IllegalArgumentException ex) {
        ProblemDto problem = new ProblemDto();
        problem.setStatus(HttpStatus.BAD_REQUEST.value());
        problem.setInstance(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problem.setType(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problem.setDetail(ex.getMessage());
        problem.setTitle(TITLE);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(problem);
    }

}
