package pl.bartoszmech.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.GithubUsersResponse;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class GithubUsersService {

    public IGithubClient client;

    public Flux<GithubUsersResponse> findAllRepositoriesByUsername(String username) {
        Flux<RepositoriesResponseAPI> userRepositories = makeRequestForUserRepositories(username);
        return userRepositories.flatMap(repo ->
            mergeAPIResponse(username, repo.name(), makeRequestForRepositoryBranches(username, repo.name())));
    }

    private Flux<RepositoriesResponseAPI> makeRequestForUserRepositories(String username) {
        log.info("Making request to fetch repositories for user: {}", username);
        Flux<RepositoriesResponseAPI> repositories = client.fetchRepositories(username);
        return filterRepositoriesByForks(repositories);
    }

    private Flux<BranchesResponseAPI> makeRequestForRepositoryBranches(String username, String repositoryName) {
        log.info("Making request to fetch branches for repository: {}/{}", username, repositoryName);
        return client.fetchBranches(username, repositoryName);
    }

    private Flux<RepositoriesResponseAPI> filterRepositoriesByForks(Flux<RepositoriesResponseAPI> repositories) {
        return repositories.filter(t -> !t.fork());
    }

    private Mono<GithubUsersResponse> mergeAPIResponse(String username, String repositoryName, Flux<BranchesResponseAPI> branches) {
        return branches
            .map(branch -> new GithubUsersResponse.Branch(branch.name(), branch.commit().sha()))
            .collectList()
            .map(branchList -> new GithubUsersResponse(repositoryName, username, branchList));
    }

}
