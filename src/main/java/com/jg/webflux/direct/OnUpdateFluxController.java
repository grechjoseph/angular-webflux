package com.jg.webflux.direct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class OnUpdateFluxController {

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    /**
     * Requests Sink to Emit provided String.
     * @param string The String to emit.
     */
    @PutMapping
    public void updateSink(@RequestBody final String string) {
        final Sinks.EmitResult result = sink.tryEmitNext(string);

        if (!Sinks.EmitResult.OK.equals(result)) {
            log.error("Failed to emit: {}", string);
        }
    }

    /**
     * Returns Flux of Sink.
     */
    @GetMapping(value = "/flux/updatable", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> updatableFlux() {
        return sink.asFlux();
    }

}
