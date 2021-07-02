package com.jg.webflux.direct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class InfiniteFluxController {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-uuuu");

    @GetMapping(value = "/server-datetime", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getServerDateTime() {
        return Flux.zip(
                Flux.fromStream(Stream.generate(() -> LocalDateTime.now().format(FORMATTER))),
                Flux.interval(Duration.ofMillis(200)),
                (key, value) -> key
        );
    }
}
