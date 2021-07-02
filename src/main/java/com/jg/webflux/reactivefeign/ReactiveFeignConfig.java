package com.jg.webflux.reactivefeign;

import org.springframework.context.annotation.Configuration;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@Configuration
@EnableReactiveFeignClients(clients = {
        SelfClient.class
})
public class ReactiveFeignConfig {
}
