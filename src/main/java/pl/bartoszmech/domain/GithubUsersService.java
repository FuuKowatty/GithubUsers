package pl.bartoszmech.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.ClientResponse;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Service
@AllArgsConstructor
public class GithubUsersService {

    public IFetcher fetcher;

    public List<ClientResponse> findAllRepositoriesByUsername(String username) {
        List<RepositoriesResponseAPI> userRepositories = makeRequestForUserRepositories(username);
        ExecutorService executorService = Executors.newCachedThreadPool();

        List<ClientResponse> clientResponses = userRepositories
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
        return clientResponses;
    }

    private List<RepositoriesResponseAPI> makeRequestForUserRepositories(String username) {
        return fetcher.fetchRepositories(username).block();
    }

    private List<BranchesResponseAPI> makeRequestForRepositoryBranches(String username, String repositoryName) {
        return fetcher.fetchBranches(username, repositoryName).block();
    }

}
