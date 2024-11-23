package com.forttiori.stokapi.contract.section;

import com.forttiori.stokapi.contract.section.response.SectionResponse;
import com.forttiori.stokapi.domain.section.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("v1/section")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @GetMapping
    public Flux<SectionResponse> list() {
        return sectionService.list()
                .map(SectionResponse::fromDomain);
    }
}
