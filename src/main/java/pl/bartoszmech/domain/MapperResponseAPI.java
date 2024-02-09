package pl.bartoszmech.domain;

import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.ClientResponse;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;

import java.util.List;

class MapperResponseAPI {

    static ClientResponse mapToClientResponse(RepositoriesResponseAPI repository, List<BranchesResponseAPI> branches) {

        if(branches.isEmpty()) {
            throw new NullPointerException("Each repository must have at least one branch");
        }

        return new ClientResponse(
            repository.name(),
            repository.owner().login(),
            branches.stream()
                .map(branch -> new ClientResponse.Branch(
                    branch.name(),
                    branch.commit().sha()))
                .toList()
        );
    }

}
