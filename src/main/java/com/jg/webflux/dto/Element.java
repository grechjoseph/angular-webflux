package com.jg.webflux.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Element {

    @Builder.Default
    private UUID elementFieldOne = UUID.randomUUID();

    @Builder.Default
    private UUID elementFieldTwo = UUID.randomUUID();

}
