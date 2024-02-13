package pl.bartoszmech.infrastructure.githubClient;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import pl.bartoszmech.domain.IGithubClient;


@Configuration
@AllArgsConstructor
public class GithubClientConfig {

    @Bean
    public RestClient getRestClient(@Value("${api.client.url}") String url) {
        return RestClient.create(url);
    }

    @Bean
    public IGithubClient createGithubClient(RestClient restClient) {
        return new GithubClientImpl(restClient);
    }

}
