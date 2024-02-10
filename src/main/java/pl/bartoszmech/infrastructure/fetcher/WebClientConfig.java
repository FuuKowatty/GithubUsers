package pl.bartoszmech.infrastructure.fetcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;
import pl.bartoszmech.domain.IFetcher;
import pl.bartoszmech.infrastructure.utils.URIBuilder;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient createGithubWebClient() {
        String GITHUB_API_URL = "https://api.github.com";
        return WebClient.builder()
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .baseUrl(GITHUB_API_URL)
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
