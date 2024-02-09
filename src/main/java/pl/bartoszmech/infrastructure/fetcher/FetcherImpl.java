package pl.bartoszmech.infrastructure.fetcher;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;
import pl.bartoszmech.domain.IFetcher;

import java.util.List;

@Component
@AllArgsConstructor
public class FetcherImpl implements IFetcher {

    private final WebClient webClient;

    @Override
    public List<RepositoriesResponseAPI> fetchRepositories(String login) {
        try {
           return webClient
                .get()
                .uri("https://api.github.com/users/" + login + "/repos")
                .retrieve()
                .toEntityList(RepositoriesResponseAPI.class)
                .block()
               .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<BranchesResponseAPI> fetchBranches(String login, String repositoryName) {
        try {
            return webClient
                .get()
                .uri("https://api.github.com/repos/"+login+"/"+repositoryName+"/branches")
                .retrieve()
                .toEntityList(BranchesResponseAPI.class)
                .block()
                .getBody();
        } catch (Exception e) {
            return null;
        }
    }

}
