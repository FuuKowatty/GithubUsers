package pl.bartoszmech.domain;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.GithubUsersResponse;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;
import pl.bartoszmech.infrastructure.exceptions.ExternalAPIException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j2
@AllArgsConstructor
public class GithubUsersService {

    public IFetcher fetcher;

    public List<GithubUsersResponse> findAllRepositoriesByUsername(String username) {
        List<RepositoriesResponseAPI> userRepositories = makeRequestForUserRepositories(username);
        ExecutorService executorService = Executors.newCachedThreadPool();

        List<GithubUsersResponse> githubUsersResponse = userRepositories
            .stream()
            .map(repo -> CompletableFuture.supplyAsync(() -> {
                List<BranchesResponseAPI> repositoryBranches = makeRequestForRepositoryBranches(repo.owner().login(), repo.name());
                return MapperResponseAPI.mapToClientResponse(repo, repositoryBranches);
            }, executorService))
            .toList()
            .stream()
            .map(CompletableFuture::join)
            .toList();

        executorService.shutdown();
        return githubUsersResponse;
    }

    private List<RepositoriesResponseAPI> makeRequestForUserRepositories(String username) {
        log.info("Making request to fetch repositories for user: {}", username);
        List<RepositoriesResponseAPI> repositories = fetcher.fetchRepositories(username).block();

        if(repositories == null) {
            String message = "Error while fetching repositories for user: " + username;
            log.error(message);
            throw new ExternalAPIException(500, message);
        }

        log.info("Received {} repositories for user: {}", repositories.size(), username);
        return repositories;
    }

    private List<BranchesResponseAPI> makeRequestForRepositoryBranches(String username, String repositoryName) {
        log.info("Making request to fetch branches for repository: {}/{} on thread {}", username, repositoryName, getCurrentThreadName());
        List<BranchesResponseAPI> branches = fetcher.fetchBranches(username, repositoryName).block();

        if(branches == null) {
            String message = "Error while fetching branches for repository: " + username + "/" + repositoryName;
            log.error(message);
            throw new ExternalAPIException(500, message);
        }

        log.info("Received {} branches for repository: {}/{} on thread {}", branches.size(), username, repositoryName, getCurrentThreadName());
        return branches;
    }

    private static String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }

}
