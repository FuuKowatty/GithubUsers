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

    public static final long THREADS_WAITING_TIME = 3;
    public IFetcher fetcher;

    public List<ClientResponse> findAllRepositoriesByUsername(String username) {
        List<RepositoriesResponseAPI> userRepositories = makeRequestForUserRepositories(username);

        List<ClientResponse> clientData = new LinkedList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(checkBestThreadNumber(userRepositories));

        userRepositories.forEach(repository -> { executorService.execute(() -> {
                    List<BranchesResponseAPI> repositoryBranches = makeRequestForRepositoryBranches(repository.owner().login(), repository.name());
                    ClientResponse clientResponse = MapperResponseAPI.mapToClientResponse(repository, repositoryBranches);
                    synchronized (clientData) {
                        clientData.add(clientResponse);
                    }
                });
        });

        executorService.shutdown();
        try {
            executorService.awaitTermination(THREADS_WAITING_TIME, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return clientData;
    }

    private static int checkBestThreadNumber(List<RepositoriesResponseAPI> userRepositories) {
        return Math.min(userRepositories.size(), Runtime.getRuntime().availableProcessors());
    }

    private List<RepositoriesResponseAPI> makeRequestForUserRepositories(String username) {
        return fetcher.fetchRepositories(username);
    }

    private List<BranchesResponseAPI> makeRequestForRepositoryBranches(String username, String repositoryName) {
        return fetcher.fetchBranches(username, repositoryName);
    }

}
