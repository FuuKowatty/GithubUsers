package pl.bartoszmech.domain;

import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;

import java.util.List;

public interface IFetcher {

    List<RepositoriesResponseAPI> fetchRepositories(String login);
    List<BranchesResponseAPI> fetchBranches(String login, String repositoryName);

}
