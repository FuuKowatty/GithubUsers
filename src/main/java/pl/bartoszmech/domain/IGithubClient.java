package pl.bartoszmech.domain;

import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;
import reactor.core.publisher.Flux;

public interface IGithubClient {

    Flux<RepositoriesResponseAPI> fetchRepositories(String username);
    Flux<BranchesResponseAPI> fetchBranches(String username, String repositoryName);

}
