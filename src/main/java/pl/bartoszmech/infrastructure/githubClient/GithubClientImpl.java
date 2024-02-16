package pl.bartoszmech.infrastructure.githubClient;

import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;
import pl.bartoszmech.domain.IGithubClient;
import pl.bartoszmech.infrastructure.exceptions.UserNotFoundException;
import pl.bartoszmech.infrastructure.utils.URIBuilder;
import reactor.core.publisher.Flux;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@AllArgsConstructor
public class GithubClientImpl implements IGithubClient {

    private final WebClient githubWebClient;

    @Override
    public Flux<RepositoriesResponseAPI> fetchRepositories(String login) {
           return githubWebClient.get()
               .uri(URIBuilder.buildRepositoryUrl(login))
               .retrieve()
               .onStatus(status -> status.isSameCodeAs(NOT_FOUND), response -> {
                   throw new UserNotFoundException(String.format("User %s not found", login));
               })
               .bodyToFlux(RepositoriesResponseAPI.class);
    }

    @Override
    public Flux<BranchesResponseAPI> fetchBranches(String login, String repositoryName) {
        return githubWebClient.get()
            .uri(URIBuilder.buildBranchesUrl(login, repositoryName))
            .retrieve()
            .bodyToFlux(BranchesResponseAPI.class);
    }

}
