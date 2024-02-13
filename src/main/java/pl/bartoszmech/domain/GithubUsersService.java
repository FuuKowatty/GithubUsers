package pl.bartoszmech.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.GithubUsersResponse;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@AllArgsConstructor
public class GithubUsersService {

    public IGithubClient fetcher;

    public List<GithubUsersResponse> findAllRepositoriesByUsername(String username) {
        List<RepositoriesResponseAPI> userRepositories = makeRequestForUserRepositories(username);
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            return userRepositories
                .stream()
                .map(repo -> fetchBranchesAsync(executorService, repo))
                .toList()
                .stream()
                .map(CompletableFuture::join)
                .toList();
        }
    }

    private CompletableFuture<GithubUsersResponse> fetchBranchesAsync(ExecutorService executor, RepositoriesResponseAPI repo) {
        return CompletableFuture.supplyAsync(() -> makeRequestForRepositoryBranches(repo.owner().login(), repo.name()), executor)
            .thenApply(branches -> mergeAPIResponse(repo, branches));
    }

    private List<RepositoriesResponseAPI> makeRequestForUserRepositories(String username) {
        log.info("Making request to fetch repositories for user: {}", username);
        List<RepositoriesResponseAPI> repositories = filterReposByForks(fetcher.fetchRepositories(username));
        log.info("Received {} repositories for user: {}", repositories.size(), username);
        return repositories;
    }

    private List<BranchesResponseAPI> makeRequestForRepositoryBranches(String username, String repositoryName) {
        log.info("Making request to fetch branches for repository: {}/{}", username, repositoryName);
        List<BranchesResponseAPI> branches = fetcher.fetchBranches(username, repositoryName);
        log.info("Received {} branches for repository: {}/{}", branches.size(), username, repositoryName);
        return branches;
    }

    private List<RepositoriesResponseAPI> filterReposByForks(List<RepositoriesResponseAPI> repositories) {
        return repositories.stream().filter(repo -> !repo.fork()).toList();
    }

    private GithubUsersResponse mergeAPIResponse(RepositoriesResponseAPI repository, List<BranchesResponseAPI> branches) {
        return new GithubUsersResponse(
            repository.name(),
            repository.owner().login(),
            branches.stream()
                .map(branch -> new GithubUsersResponse.Branch(
                    branch.name(),
                    branch.commit().sha()))
                .toList()
        );
    }

}
