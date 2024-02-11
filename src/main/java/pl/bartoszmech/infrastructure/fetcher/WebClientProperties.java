package pl.bartoszmech.infrastructure.fetcher;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "fetcher")
public record WebClientProperties(
    String url,
    Integer connectionTimeout,
    Integer readTimeout
) {}
