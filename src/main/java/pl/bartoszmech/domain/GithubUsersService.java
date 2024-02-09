package pl.bartoszmech.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.ClientResponse;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@AllArgsConstructor
public class GithubUsersService {

    public IFetcher fetcher;

    public List<ClientResponse> findAllRepositoriesByUsername(String username) {
        List<RepositoriesResponseAPI> userRepositories = makeRequestForUserRepositories(username);

        List<ClientResponse> clientData = new LinkedList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(userRepositories.size());

        userRepositories.forEach(repository -> {
            executorService
                .execute(() -> {
                    List<BranchesResponseAPI> repositoryBranches = makeRequestForRepositoryBranches(repository.owner().login(), repository.name());
                    ClientResponse clientResponse = MapperResponseAPI.mapToClientResponse(repository, repositoryBranches);
                    System.out.println(clientResponse);
                    clientData.add(clientResponse);
                });
        });

        try {
            executorService.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return clientData;
    }

    private List<RepositoriesResponseAPI> makeRequestForUserRepositories(String username) {
        List<RepositoriesResponseAPI> userRepositories = fetcher.fetchRepositories(username);

        if(userRepositories == null) {
            throw new UserNotFoundException("User with the specified login could not be found");
        }

        return userRepositories;
    }

    private List<BranchesResponseAPI> makeRequestForRepositoryBranches(String username, String repositoryName) {
        return fetcher.fetchBranches(username, repositoryName);
    }

}
