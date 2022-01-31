package com.visable.messagingservice.controller;

import com.visable.messagingservice.model.MessageDto;
import com.visable.messagingservice.model.MessagesDto;

import java.util.UUID;
import com.visable.messagingservice.model.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

/**
 * A delegate to be called by the {@link MessagingServiceApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */

public interface MessagingServiceApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * @see MessagingServiceApi#createUser
     */
    default ResponseEntity<Void> createUser(UserDto userDto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * @see MessagingServiceApi#getReceivedMessages
     */
    default ResponseEntity<MessagesDto> getReceivedMessages(UUID userId,
        Integer pageNumber,
        Integer pageSize) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"pagination\" : {    \"total\" : 1,    \"pageNumber\" : 0,    \"pageSize\" : 6  },  \"messages\" : [ {    \"sentTo\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"sentOn\" : \"2000-01-23T04:56:07.000+00:00\",    \"content\" : \"content\",    \"sentBy\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\"  }, {    \"sentTo\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"sentOn\" : \"2000-01-23T04:56:07.000+00:00\",    \"content\" : \"content\",    \"sentBy\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\"  } ]}");
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * @see MessagingServiceApi#getReceivedMessagesFromParticularUser
     */
    default ResponseEntity<MessagesDto> getReceivedMessagesFromParticularUser(UUID userId,
        UUID userIdToSearchBy,
        Integer pageNumber,
        Integer pageSize) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"pagination\" : {    \"total\" : 1,    \"pageNumber\" : 0,    \"pageSize\" : 6  },  \"messages\" : [ {    \"sentTo\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"sentOn\" : \"2000-01-23T04:56:07.000+00:00\",    \"content\" : \"content\",    \"sentBy\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\"  }, {    \"sentTo\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"sentOn\" : \"2000-01-23T04:56:07.000+00:00\",    \"content\" : \"content\",    \"sentBy\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\"  } ]}");
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * @see MessagingServiceApi#getSentMessages
     */
    default ResponseEntity<MessagesDto> getSentMessages(UUID userId,
        Integer pageNumber,
        Integer pageSize) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    ApiUtil.setExampleResponse(request, "application/json", "{  \"pagination\" : {    \"total\" : 1,    \"pageNumber\" : 0,    \"pageSize\" : 6  },  \"messages\" : [ {    \"sentTo\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"sentOn\" : \"2000-01-23T04:56:07.000+00:00\",    \"content\" : \"content\",    \"sentBy\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\"  }, {    \"sentTo\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\",    \"sentOn\" : \"2000-01-23T04:56:07.000+00:00\",    \"content\" : \"content\",    \"sentBy\" : \"046b6c7f-0b8a-43b9-b35d-6489e6daee91\"  } ]}");
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * @see MessagingServiceApi#sendMessage
     */
    default ResponseEntity<Void> sendMessage(UUID userId,
        MessageDto messageDto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
