package pl.bartoszmech.domain;

import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFetcher {

    Mono<List<RepositoriesResponseAPI>> fetchRepositories(String username);
    Mono<List<BranchesResponseAPI>> fetchBranches(String username, String repositoryName);

}
