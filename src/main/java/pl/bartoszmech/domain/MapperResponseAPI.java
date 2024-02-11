package pl.bartoszmech.domain;

import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.GithubUsersResponse;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;

import java.util.List;

class MapperResponseAPI {

    static GithubUsersResponse mapToClientResponse(RepositoriesResponseAPI repository, List<BranchesResponseAPI> branches) {
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
