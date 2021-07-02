package com.jg.webflux.reactivefeign;

import com.jg.webflux.dto.Page;
import com.jg.webflux.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.logging.Level;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReactiveFeignController {

    private final SelfClient selfClient;

    /**
     * Consuming Flux endpoint using ReactiveFeign.
     */
    @PostMapping(value = "/flux/reactive-feign", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Page> fluxAllPagesPOSTReactive(@RequestBody final PostRequest request) {
        log.debug("Getting all pages from ReactiveFeign.");
        final Flux<Page> pageFlux = selfClient.fluxAllPagesPOST(request)
                .log("reactive-flux", Level.INFO, SignalType.ON_NEXT);
        log.debug("Returning Flux retrieved from ReactiveFeign.");
        return pageFlux;
    }

}
