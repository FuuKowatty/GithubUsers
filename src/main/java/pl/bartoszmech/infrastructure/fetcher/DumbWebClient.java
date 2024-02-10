package pl.bartoszmech.infrastructure.fetcher;

import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;
import pl.bartoszmech.domain.IFetcher;
import reactor.core.publisher.Mono;

import java.util.List;

public class DumbWebClient implements IFetcher {

    @Override
    public Mono<List<RepositoriesResponseAPI>> fetchRepositories(String username) {
        List<RepositoriesResponseAPI> hardcodedRepositories = List.of(
            new RepositoriesResponseAPI("repository-1", new RepositoriesResponseAPI.Owner("owner-1")),
            new RepositoriesResponseAPI("repository-2", new RepositoriesResponseAPI.Owner("owner-2")),
            new RepositoriesResponseAPI("repository-3", new RepositoriesResponseAPI.Owner("owner-3")),
            new RepositoriesResponseAPI("repository-4", new RepositoriesResponseAPI.Owner("owner-4"))
        );
        return Mono.just(hardcodedRepositories);
    }

    @Override
    public Mono<List<BranchesResponseAPI>> fetchBranches(String username, String repositoryName) {
        List<BranchesResponseAPI> hardcodedBranches = List.of(
            new BranchesResponseAPI("branch-1", new BranchesResponseAPI.Commit("sha-1")),
            new BranchesResponseAPI("branch-2", new BranchesResponseAPI.Commit("sha-2")),
            new BranchesResponseAPI("branch-3", new BranchesResponseAPI.Commit("sha-3"))
        );
        return Mono.just(hardcodedBranches);
    }

}
