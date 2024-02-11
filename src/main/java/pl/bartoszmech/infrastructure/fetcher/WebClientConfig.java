package pl.bartoszmech.infrastructure.fetcher;

import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import pl.bartoszmech.domain.IFetcher;
import pl.bartoszmech.infrastructure.utils.URIBuilder;

import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import io.netty.handler.timeout.ReadTimeoutHandler;

@Configuration
@AllArgsConstructor
public class WebClientConfig {

    private final WebClientProperties properties;

    @Bean
    public HttpClient getHttpClient() {
        return HttpClient.create()
            .option(CONNECT_TIMEOUT_MILLIS, properties.connectionTimeout())
            .responseTimeout(Duration.ofMillis(properties.readTimeout()))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(5000, MILLISECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(5000, MILLISECONDS)));
    }

    @Bean
    public WebClient createGithubWebClient() {
        return WebClient.builder()
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .baseUrl(properties.url())
            .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
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
