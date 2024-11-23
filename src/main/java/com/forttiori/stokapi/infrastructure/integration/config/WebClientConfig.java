package com.forttiori.stokapi.infrastructure.integration.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


@Configuration
public class WebClientConfig {

    @Value( "${api.base.url}" )
    private String baseUrl;

    @Value( "${api.auth.token}" )
    private String token;


    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers ->
                        headers.add("Authorization", "Bearer " + token))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
