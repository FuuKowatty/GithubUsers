package pl.bartoszmech.domain;

import pl.bartoszmech.infrastructure.fetch.FetcherTestImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.bartoszmech.application.response.BranchesResponseAPI;
import pl.bartoszmech.application.response.ClientResponse;
import pl.bartoszmech.application.response.RepositoriesResponseAPI;

import java.util.Collections;
import java.util.List;

public class MapperResponseAPITest {

    IFetcher fetcher = new FetcherTestImpl();

    @Test
    public void should_map_to_ClientResponseAPI() {
        //given
        String login = "Login1";
        List<RepositoriesResponseAPI> repositories = fetcher.fetchRepositories(login).block();
        RepositoriesResponseAPI repositoryToFind = repositories.getFirst();
        List<BranchesResponseAPI> branches = fetcher.fetchBranches(login, repositoryToFind.name()).block();

        //when
        ClientResponse clientResponse = MapperResponseAPI.mapToClientResponse(repositoryToFind, branches);

        //then
        Assertions.assertEquals(login, clientResponse.ownerLogin());
        Assertions.assertEquals(repositoryToFind.name(), clientResponse.repositoryName());
        Assertions.assertEquals(branches.size(), clientResponse.branches().size());
    }

    @Test
    public void should_throw_error_if_branches_are_null() {
        //given
        String login = "Login1";
        List<RepositoriesResponseAPI> repositories = fetcher.fetchRepositories(login).block();
        RepositoriesResponseAPI repositoryToFind = repositories.getFirst();

        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () -> MapperResponseAPI.mapToClientResponse(repositoryToFind, null));
    }

    @Test
    public void should_throw_error_if_repository_is_null() {
        //given
        String login = "Login1";
        List<BranchesResponseAPI> branches = fetcher.fetchBranches(login, "Repository 1").block();

        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () -> MapperResponseAPI.mapToClientResponse(null, branches));
    }

    @Test
    public void should_throw_error_if_branches_is_empty_array() {
        //given
        String login = "Login1";
        List<RepositoriesResponseAPI> repositories = fetcher.fetchRepositories(login).block();
        RepositoriesResponseAPI repositoryToFind = repositories.getFirst();

        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () -> MapperResponseAPI.mapToClientResponse(repositoryToFind, Collections.emptyList()));
    }

}
