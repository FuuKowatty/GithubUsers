package pl.bartoszmech.infrastructure.fetcher;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;
import pl.bartoszmech.domain.IFetcher;
import pl.bartoszmech.infrastructure.utils.URIBuilder;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@AllArgsConstructor
public class WebClientConfig {

    private final WebClientProperties properties;

    @Bean
    public WebClient createGithubWebClient() {
        return WebClient.builder()
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .baseUrl(properties.url())
            .build();
    }

    @Bean
    @Profile("!dev")
    public IFetcher createGithubFetcher(WebClient webClient, URIBuilder uriBuilder) {
        return new FetcherImpl(webClient, uriBuilder);
    }

    @Bean
    @Profile("dev")
    public IFetcher createDumbWebClientFetcher(WebClient webClient, URIBuilder uriBuilder) {
        return new DumbWebClient();
    }

}
