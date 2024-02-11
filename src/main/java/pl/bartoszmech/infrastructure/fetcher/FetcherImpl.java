package pl.bartoszmech.infrastructure.fetcher;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;
import pl.bartoszmech.domain.IFetcher;
import pl.bartoszmech.infrastructure.exceptions.ExternalAPIException;
import pl.bartoszmech.infrastructure.exceptions.UserNotFoundException;
import pl.bartoszmech.infrastructure.utils.URIBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class FetcherImpl implements IFetcher {

    private final WebClient githubWebClient;
    private final URIBuilder uriBuilder;

    @Override
    public Mono<List<RepositoriesResponseAPI>> fetchRepositories(String login) {
           return githubWebClient
                .get()
                .uri(uriBuilder.buildRepositoryUrl(login))
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(new ParameterizedTypeReference<List<RepositoriesResponseAPI>> () {});
    }

    @Override
    public Mono<List<BranchesResponseAPI>> fetchBranches(String login, String repositoryName) {
            return githubWebClient
                .get()
                .uri(uriBuilder.buildBranchesUrl(login, repositoryName))
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(new ParameterizedTypeReference<List<BranchesResponseAPI>> () {});
    }

    private Mono<? extends Throwable> handleError(ClientResponse response) {
        return switch (response.statusCode().value()) {
            case 403 -> Mono.error(new ExternalAPIException(403, "Limit from external API was exceed"));
            case 404 -> Mono.error(new UserNotFoundException("User not found"));
            case 500 -> Mono.error(new ExternalAPIException(500, "External server error"));
            default -> Mono.error(new ExternalAPIException(response.statusCode().value(), "Something went wrong while fetching data"));
        };
    }

}
