package pl.bartoszmech.infrastructure.fetch;

import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;
import pl.bartoszmech.domain.IFetcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetcherTestImpl implements IFetcher {

    Map<String, List<RepositoriesResponseAPI>> allRepositories = new HashMap<>(
        Map.of(
            "Login1", List.of(
                new RepositoriesResponseAPI("Repository 1", new RepositoriesResponseAPI.Owner("Login1")),
                new RepositoriesResponseAPI("Repository 2", new RepositoriesResponseAPI.Owner("Login1")),
                new RepositoriesResponseAPI("Repository 3", new RepositoriesResponseAPI.Owner("Login1"))
            ),
            "Login2", Collections.emptyList(),
            "Login3", List.of(
                new RepositoriesResponseAPI("Repository 1", new RepositoriesResponseAPI.Owner("Login3"))
            )
        )
    );
    Map<String, List<BranchesResponseAPI>> allBranches = new HashMap<>(
        Map.of(
            "Repository 1", List.of(
                new BranchesResponseAPI("main", new BranchesResponseAPI.Commit("123456abcdef0123456")),
                new BranchesResponseAPI("develop", new BranchesResponseAPI.Commit("987654321fedcba09876"))
            ),
            "Repository 2", List.of(new BranchesResponseAPI("master", new BranchesResponseAPI.Commit("123456abcdef0123456"))),
            "Repository 3", List.of(new BranchesResponseAPI("master", new BranchesResponseAPI.Commit("123456abcdef01239376")))
            )
    );


    @Override
    public List<RepositoriesResponseAPI> fetchRepositories(String login) {
        return allRepositories.get(login);
    }

    @Override
    public List<BranchesResponseAPI> fetchBranches(String login, String repositoryName) {
        return allBranches.get(repositoryName);
    }

}
