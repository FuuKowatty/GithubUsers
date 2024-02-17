package pl.bartoszmech.infrastructure.githubClient;

import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import pl.bartoszmech.domain.IGithubClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import io.netty.handler.timeout.ReadTimeoutHandler;


@Configuration
@AllArgsConstructor
public class GithubClientConfig {

    @Bean
    public HttpClient getHttpClient() {
        return HttpClient.create()
            .option(CONNECT_TIMEOUT_MILLIS, 1000)
            .responseTimeout(Duration.ofMillis(2500))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(5000, MILLISECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(5000, MILLISECONDS)));
    }

    @Bean
    public WebClient createGithubWebClient(@Value("${api.client.url}") String url) {
        return WebClient.builder()
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .baseUrl(url)
            .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
            .build();
    }


    @Bean
    public IGithubClient createGithubFetcher(WebClient webClient) {
        return new GithubClientImpl(webClient);
    }

}
