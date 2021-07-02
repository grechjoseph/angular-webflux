package com.jg.webflux.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    private int page;
    private int pageSize;
    private int totalPages;
    private int totalElements;
    private List<Element> elements;

}
