package com.visable.messagingService.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;

@Controller
@RequestMapping("${openapi.messsagingService.base-path:/api/messaging-service}")
public class MessagingServiceApiController implements MessagingServiceApi {

    private final MessagingServiceApiDelegate delegate;

    public MessagingServiceApiController(@org.springframework.beans.factory.annotation.Autowired(required = false) MessagingServiceApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new MessagingServiceApiDelegate() {});
    }

    @Override
    public MessagingServiceApiDelegate getDelegate() {
        return delegate;
    }

}
