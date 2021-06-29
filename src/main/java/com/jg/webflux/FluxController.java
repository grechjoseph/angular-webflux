package com.jg.webflux;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RestController
@CrossOrigin("http://localhost:4200")
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
        return Flux.range(1, request.getTotalPages())
                .map(pageNumber -> {
                    /*
                        Whatever happens here is done sequentially, ie: each page is processed (garbage collected) before
                        proceeding to the next page.
                     */
                    log.debug("Getting page: {}", pageNumber);
                    sleep(500L);
                    final Page pageToPublishg = getPage(pageNumber, request.getElementsPerPage(), request.getTotalPages());
                    log.debug("Publishing page: {}", pageNumber);
                    sleep(500L);
                    return pageToPublishg;
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PostRequest {
        private int totalPages;
        private int elementsPerPage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Page {

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
    private static class Element {

        @Builder.Default
        private UUID elementFieldOne = UUID.randomUUID();

        @Builder.Default
        private UUID elementFieldTwo = UUID.randomUUID();

    }

}
