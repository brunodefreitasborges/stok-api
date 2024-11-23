package com.forttiori.stokapi.contract.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record AvailableDatesResponse(
        List<LocalDate> dates
) {
    public static AvailableDatesResponse fromDomain(List<LocalDate> dates) {
        return AvailableDatesResponse.builder()
                .dates(dates.stream().sorted().toList())
                .build();
    }
}
