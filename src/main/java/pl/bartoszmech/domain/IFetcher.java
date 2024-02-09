package pl.bartoszmech.domain;

import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;

import java.util.List;

public interface IFetcher {

    List<RepositoriesResponseAPI> fetchRepositories(String username);
    List<BranchesResponseAPI> fetchBranches(String username, String repositoryName);

}
