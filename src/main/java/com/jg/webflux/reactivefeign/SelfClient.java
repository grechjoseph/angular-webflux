package com.jg.webflux.reactivefeign;

import com.jg.webflux.dto.Page;
import com.jg.webflux.dto.PostRequest;
import org.springframework.web.bind.annotation.PostMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;

@ReactiveFeignClient(
        name = "${reactive-feign.self-client.name:self-client}",
        url = "${reactive-feign.self-client.url:http://localhost:8080}"
)
public interface SelfClient {

    @PostMapping("/flux")
    Flux<Page> fluxAllPagesPOST(final PostRequest request);

}
