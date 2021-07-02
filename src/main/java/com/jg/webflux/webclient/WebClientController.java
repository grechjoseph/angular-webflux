package com.jg.webflux.webclient;

import com.jg.webflux.dto.Page;
import com.jg.webflux.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.logging.Level;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class WebClientController {

    /**
     * Consuming Flux endpoint using {@link WebClient}.
     */
    @PostMapping(value = "/flux/web-client", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Page> fluxAllPagesPOSTBridged(@RequestBody final PostRequest request) {
        log.debug("Getting all pages from WebClient.");
        final WebClient webClient = WebClient.create("http://localhost:8080");
        return webClient
                .post()
                .uri("/flux")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToFlux(Page.class)
                .log("bridged-flux", Level.INFO, SignalType.ON_NEXT);
    }

}
