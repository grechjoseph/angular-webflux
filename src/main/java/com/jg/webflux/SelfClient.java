package com.jg.webflux;

import org.springframework.web.bind.annotation.PostMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;

@ReactiveFeignClient(name = "self-client", url = "http://localhost:8080")
public interface SelfClient {

    @PostMapping("/flux")
    Flux<FluxController.Page> fluxAllPagesPOST(final FluxController.PostRequest request);

}
