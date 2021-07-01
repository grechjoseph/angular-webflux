package com.jg.webflux;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class FluxController {

    /**
     * A Flux is the counter part of a Collection in Webflux, where it can contain 0..N elements.
     */
    @GetMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Page> fluxAllPages() {
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
        log.debug("Getting all pages.");
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

    /**
     * Consuming Flux endpoint using {@link WebClient}.
     */
    @PostMapping(value = "/flux/bridged", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Page> fluxAllPagesPOSTBridged(@RequestBody final PostRequest request) {
        log.debug("Initializing WebClient.");
        final WebClient webClient = WebClient.create("http://localhost:8080");
        log.debug("WebClient initialized.");
        return webClient
                .post()
                .uri("/flux")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToFlux(Page.class)
                .map(page -> {
                    log.debug("Page {} at bridge.", page.getPage());
                    return page;
                });
    }

    private final SelfClient selfClient;

    /**
     * Consuming Flux endpoint using ReactiveFeign.
     */
    @PostMapping(value = "/flux/reactive", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Page> fluxAllPagesPOSTReactive(@RequestBody final PostRequest request) {
        log.debug("Fetching Flux through ReactiveFeign.");
        final Flux<Page> pageFlux = selfClient.fluxAllPagesPOST(request)
                .map(page -> {
                    log.debug("Page {} at reactive.", page.getPage());
                    return page;
                });
        log.debug("Returning Flux retrieved through ReactiveFeign.");
        return pageFlux;
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class PostRequest {
        private int totalPages;
        private int elementsPerPage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Page {

        private int page;
        private int pageSize;
        private int totalPages;
        private int totalElements;
        private List<Element> elements;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Element {

        @Builder.Default
        private UUID elementFieldOne = UUID.randomUUID();

        @Builder.Default
        private UUID elementFieldTwo = UUID.randomUUID();

    }

}
