package pl.bartoszmech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.bartoszmech.infrastructure.fetcher.WebClientProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = WebClientProperties.class)
public class GithubUsers {
    public static void main(String[] args) {
        SpringApplication.run(GithubUsers.class, args);
    }
}