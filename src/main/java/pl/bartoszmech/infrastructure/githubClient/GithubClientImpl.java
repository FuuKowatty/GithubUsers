package pl.bartoszmech.infrastructure.githubClient;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;
import pl.bartoszmech.domain.IGithubClient;
import pl.bartoszmech.infrastructure.exceptions.UserNotFoundException;
import pl.bartoszmech.infrastructure.utils.URIBuilder;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@AllArgsConstructor
public class GithubClientImpl implements IGithubClient {

    private final RestClient githubRestClient;

    @Override
    public List<RepositoriesResponseAPI> fetchRepositories(String login) {
           return githubRestClient.get()
               .uri(URIBuilder.buildRepositoryUrl(login))
               .retrieve()
               .onStatus(status -> status.isSameCodeAs(NOT_FOUND), (request, response) -> {
                   throw new UserNotFoundException(String.format("User %s not found", login));
               })
               .body(new ParameterizedTypeReference<>(){});
    }

    @Override
    public List<BranchesResponseAPI> fetchBranches(String login, String repositoryName) {
        return githubRestClient.get()
            .uri(URIBuilder.buildBranchesUrl(login, repositoryName))
            .retrieve()
            .body(new ParameterizedTypeReference<>(){});
    }

}
