package com.jg.webflux.direct;

import com.jg.webflux.dto.Element;
import com.jg.webflux.dto.Page;
import com.jg.webflux.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class FluxController {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-uuuu");

    @GetMapping(value = "/server-datetime", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getServerDateTime() {
        return Flux.zip(
                Flux.fromStream(Stream.generate(() -> LocalDateTime.now().format(FORMATTER))),
                Flux.interval(Duration.ofMillis(200)),
                (key, value) -> key
        );
    }

    /**
     * A Flux is the counter part of a Collection in Webflux, where it can contain 0..N elements.
     */
    @GetMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Page> fluxAllPages() {
        log.debug("Getting all pages from GET Method.");
        return fluxAllPagesPOST(PostRequest.builder()
                .totalPages(5)
                .elementsPerPage(5)
                .build());
    }

    /**
     * Alternative method of type Http POST which also accepts a Request Body.
     * @param request The Request Body.
     * @return Text Event Stream with pages.
     */
    @PostMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Page> fluxAllPagesPOST(@RequestBody final PostRequest request) {
        log.debug("Getting all pages from POST Method.");
        return Flux.range(1, request.getTotalPages())
                .map(pageNumber -> {
                    /*
                        Whatever happens here is done sequentially, ie: each page is processed (garbage collected) before
                        proceeding to the next page.
                     */
                    sleep(500L);
                    final Page pageToPublish = getPage(pageNumber, request.getElementsPerPage(), request.getTotalPages());
                    log.debug("Publishing page: {}", pageNumber);
                    return pageToPublish;
                });
    }

    private void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private Page getPage(final int pageNumber,
                         final int pageSize,
                         final int totalPages) {
        return Page.builder()
                .page(pageNumber)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .totalElements(totalPages * pageSize)
                .elements(IntStream.range(0, pageSize).mapToObj(i -> Element.builder().build()).collect(Collectors.toList()))
                .build();
    }

}
