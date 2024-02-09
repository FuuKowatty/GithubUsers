package pl.bartoszmech.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.ClientResponse;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class GithubUsersService {

    public IFetcher fetcher;

    public List<ClientResponse> findAllRepositoriesByUsername(String username) {
        long start = System.currentTimeMillis();
        List<RepositoriesResponseAPI> userRepositories = makeRequestForUserRepositories(username);

        List<ClientResponse> clientData = new LinkedList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(userRepositories.size());

        userRepositories.forEach(repository -> {
            executorService
                .execute(() -> {
                    List<BranchesResponseAPI> repositoryBranches = makeRequestForRepositoryBranches(repository.owner().login(), repository.name());
                    ClientResponse clientResponse = MapperResponseAPI.mapToClientResponse(repository, repositoryBranches);
                    clientData.add(clientResponse);
                });
        });

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            //
        }
        System.out.println("Time taken: " + (System.currentTimeMillis() - start) + " ms");
        return clientData;
    }

    private List<RepositoriesResponseAPI> makeRequestForUserRepositories(String username) {
        return fetcher.fetchRepositories(username);
    }

    private List<BranchesResponseAPI> makeRequestForRepositoryBranches(String username, String repositoryName) {
        return fetcher.fetchBranches(username, repositoryName);
    }

}
